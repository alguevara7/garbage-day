(ns garbageday.test.rest
  (:use [garbageday.rest])
  (:use [clojure.test])
  (:require [cheshire.core :as json]))

(defn request [resource routes & params]
     (routes {:request-method :get :uri resource :params (first params)}))

;re-write to test each individual element. test existence and absense
(deftest test-routes
  (is (= 200 (:status (request "/api/1.0/lg=1.1&lt=2.2" routes))) "return status 200-OK")
  (with-redefs [garbageday.model/garbage-day (fn [lg lt] "Monday")]
    (is (= "{\"day-of-week\":\"Monday\"}" (:body (request "/api/1.0/lg=1.1&lt=2.2" routes))) "match url and parameters"))
  (with-redefs [garbageday.model/garbage-day (fn [lg lt] "Monday")]
    (is (= "{\"day-of-week\":\"Monday\"}" (:body (request "/api/1.0/lg=-1.1&lt=-2.2" routes))) "match url and negative parameters"))
  (is (nil? (request "/" routes)) "no query and no parameters")
  (is (nil? (request "/api/1.0/lg=11&lt=2.2" routes)) "longitude missing decimal")
  (is (nil? (request "/api/1.0/lg=1.1&lt=22" routes)) "latitude missing decimal")
  (is (nil? (request "/other/1.0/lg=11&lt=2.2" routes)) "miss-matched query")
  (is (nil? (request "/api/x.x/lg=11&lt=2.2" routes)) "miss-matched version")
  )

