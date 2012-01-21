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
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["1.1" "2.2"]) "Monday")]
    (is (= 200 (:status (request "/api/1.0/lg=1.1&lt=2.2" routes)))
        "status 200 for properly formed request, with positive longitude and latitude"))
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["-1.1" "-2.2"]) "Monday")]
    (is (= 200 (:status (request "/api/1.0/lg=-1.1&lt=-2.2" routes)))
        "status 200 for properly formed request, with negative longitude and latitude"))
  )

(deftest test-response-encoding
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["1.1" "2.2"]) "Monday")]
    (is (= "{\"day-of-week\":\"Monday\"}" (:body (request "/api/1.0/lg=1.1&lt=2.2" routes)))
        "correct body for properly formed request, with positive longitude and latitude"))
  (with-redefs [garbageday.model/collection-schedule #(if (= [%1 %2] ["-1.1" "-2.2"]) "Monday")]
    (is (= "{\"day-of-week\":\"Monday\"}" (:body (request "/api/1.0/lg=-1.1&lt=-2.2" routes)))
        "correct body for properly formed request, with negative longitude and latitude"))
  )