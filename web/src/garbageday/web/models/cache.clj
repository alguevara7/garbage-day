(ns garbageday.web.models.cache
  (:use clojure.core)
  (:require [clojure.string :as s])
  (:import (net.spy.memcached.auth AuthDescriptor PlainCallbackHandler)
           (net.spy.memcached ConnectionFactoryBuilder MemcachedClient AddrUtil ConnectionFactoryBuilder$Protocol)))

;;fix with-open (rename to ...)

(def ^{:dynamic true} *cache* {:client nil})

(defn find-client "Returns the current memcached client (or nil if there is none)"  [] (:client *cache*))

(defn get-client [{:keys [hosts username password]}]
  (let [auth-desc (AuthDescriptor. (into-array String ["PLAIN"]) (PlainCallbackHandler. username password))
        builder (doto (ConnectionFactoryBuilder.) (.setAuthDescriptor auth-desc) (.setProtocol ConnectionFactoryBuilder$Protocol/BINARY))]
    (MemcachedClient. (.build builder) (AddrUtil/getAddresses hosts))))

(defn- to-key [address year month day]
  (str (s/replace address " " "_") year month day))

(defn get-value [address year month day]
  (try (read-string (.get (find-client) (to-key address year month day))) (catch Exception e nil)))

; add print-dup support for org.joda.time.DateTime
(defmethod print-dup org.joda.time.ReadableInstant [o w]
  (print-ctor o (fn [o w] (print-dup (.getMillis  o) w)) w))

(defn put [address year month day collection-info]
  (let [future (.set (find-client) (to-key address year month day) 0 (str (binding [*print-dup* true] (prn-str collection-info))))]
    (.get future))) ; wait for future

(defmacro ^{:private true} assert-args [fnname & pairs]
  `(do (when-not ~(first pairs)
         (throw (IllegalArgumentException. ~(str fnname " requires " (second pairs)))))
       ~(let [more (nnext pairs)]
          (when more (list* `assert-args fnname more)))))

; extend this withopen to receive the name of the method that should be called to release resources
(defmacro ^{:private true} with-open1
  "bindings => [name init ...]

  Evaluates body in a try expression with names bound to the values
  of the inits, and a finally clause that calls (.shutdown name) on each
  name in reverse order."
  {:added "1.0"}
  [bindings & body]
  (assert-args with-open
     (vector? bindings) "a vector for its binding"
     (even? (count bindings)) "an even number of forms in binding vector")
  (cond
    (= (count bindings) 0) `(do ~@body)
    (symbol? (bindings 0)) `(let ~(subvec bindings 0 2)
                              (try
                                (with-open ~(subvec bindings 2) ~@body)
                                (finally
                                  (. ~(bindings 0) shutdown))))
    :else (throw (IllegalArgumentException. "with-open only allows Symbols in bindings"))))

(defmacro with-memcached
  ""
  [spec & body]
  `(with-memcached* ~spec (fn [] ~@body)))

(defn with-memcached*
  "Evaluates func in the context of a new client connection to a memcached then closes the connection."
  [spec func]
  (with-open1 [^MemcachedClient client (get-client spec)]
    (binding [*cache* (assoc *cache* :client client)]
      (func))))


