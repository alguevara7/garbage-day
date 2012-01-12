(ns garbageday.core
  (:use compojure.core)
  (:require [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [garbageday.rest]))

(defroutes urls
  garbageday.rest/routes
  (route/not-found "Page not Found!"))

(def run-server (handler/api urls))

(defn start [port]
  (ring/run-jetty (var run-server)
                  {:port (or port 8080) :join? false}))

(defn -main
  ([] (-main 8080))
  ([port]
     (let [sys-port (System/getenv "PORT")]
       (if (nil? sys-port)
         (start (cond
                 (string? port) (Integer/parseInt port)
                 :else port))
         (start (Integer/parseInt sys-port))))))

