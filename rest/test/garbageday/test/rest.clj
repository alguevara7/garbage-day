(ns garbageday.test.rest
  (:use [garbageday.rest])
  (:use [clojure.test])
  (:require [cheshire.core :as json]))

(defn- request [resource routes & params]
     (routes {:request-method :get :uri resource :params (first params)}))

(deftest test-request-dispatch
  (is (nil? (request "/" routes)) "no query and no parameters")
  (is (nil? (request "/api/1.0/lg=11&lt=2.2" routes)) "longitude missing decimal")
  (is (nil? (request "/api/1.0/lg=1.1&lt=22" routes)) "latitude missing decimal")
  (is (nil? (request "/other/1.0/lg=1.1&lt=2.2" routes)) "api suffix missing")
  (is (nil? (request "/api/x.x/lg=1.1&lt=2.2" routes)) "version missing")
  (is (nil? (request "/api/1.0/lg=1.1&lt=2.2&y=a&m=1&d=1" routes)) "invalid year")
  (is (nil? (request "/api/1.0/lg=1.1&lt=2.2&y=2001&m=a&d=1" routes)) "invalid month")
  (is (nil? (request "/api/1.0/lg=1.1&lt=2.2&y=2001&m=1&d=a" routes)) "invalid day")
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["1.1" "2.2"]) "Tuesday 2")]
    (is (= 200 (:status (request "/api/1.0/lg=1.1&lt=2.2&y=2012&m=1&d=1" routes)))
        "status 200 for properly formed request, with positive longitude and latitude"))
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["-1.1" "-2.2"]) "Tuesday 2")]
    (is (= 200 (:status (request "/api/1.0/lg=-1.1&lt=-2.2&y=2012&m=1&d=1" routes)))
        "status 200 for properly formed request, with negative longitude and latitude"))
  )

(deftest test-response-encoding
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["1.1" "2.2"]) "Tuesday 2")]
    (is (= "{\"day-of-week\":\"Tuesday\",\"what-is-collected\":[\"green-bin\",\"recycling\"]}" (:body (request "/api/1.0/lg=1.1&lt=2.2&y=2012&m=1&d=3" routes)))
        "correct body for properly formed request, with positive longitude and latitude"))
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["-1.1" "-2.2"]) "Tuesday 2")]
    (is (= "{\"day-of-week\":\"Tuesday\",\"what-is-collected\":[\"green-bin\",\"recycling\"]}" (:body (request "/api/1.0/lg=-1.1&lt=-2.2&y=2012&m=1&d=3" routes)))
        "correct body for properly formed request, with negative longitude and latitude"))
  )