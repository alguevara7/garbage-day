(ns garbageday.web.models.location
  (:use [hiccup.page-helpers :only [url]])
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [noir.validation :as vali]))

; improve idiom to traverse json response
(defn geo-locate [address]
  (let [response (client/get (url "http://maps.googleapis.com/maps/api/geocode/json" {:address address :sensor "false"}))
        json-response (json/parse-string (:body response))
        location (get (get (first (get json-response "results")) "geometry") "location")]
        {:latitude (get location "lat") :longitude (get location "lng")}))

