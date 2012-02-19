(ns garbageday.web.models.schedule
  (:use [hiccup.page-helpers :only [url]])
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(defn search [longitude latitude year month day]
  (let [response (client/get (str "http://garbageday-rest.herokuapp.com/whatiscollected/1.0/"
                                  "lg=" longitude "&lt=" latitude "&y=" year "&m=" month "&d=" day))]
    (json/parse-string (:body response))))

; improve idiom to traverse json response
(defn geo-locate [address]
  (let [response (client/get (url "http://maps.googleapis.com/maps/api/geocode/json" {:address address :sensor "false"}))
        json-response (json/parse-string (:body response))
        location (get (get (first (get json-response "results")) "geometry") "location")]
    {:latitude (get location "lat") :longitude (get location "lng")}))