(ns garbageday.web.models.schedule
  (:use [hiccup.page-helpers :only [url]])
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(defn search [longitude latitude year month day]
  (let [response (client/get (str "http://garbageday-rest.herokuapp.com/whatiscollected/1.0/"
                                  "lg=" longitude "&lt=" latitude "&y=" year "&m=" month "&d=" day))]
    (json/parse-string (:body response))))

(defn geo-locate [address]
  {:longitude "79.410950" :latitude "43.667957"})