(ns garbageday.web.models.test.cache
  (:use [clojure.test])
  (:require [garbageday.web.models.cache :as cache]))

(deftest set
  (cache/with-memcached {:hosts "localhost:11211" :username "username" :password "password"}
    (is (= nil (cache/get "no address" 2011 1 1)) "key does not exist in the cache")
    (is (= {:a 1} (do (cache/put "77 albany" 2011 1 2 {:a 1})
                  (cache/get "77 albany" 2011 1 2))) "key found")))
