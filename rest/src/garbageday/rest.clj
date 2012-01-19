(ns garbageday.rest
  (:use [compojure.core :only [defroutes GET]])
  (:use ring.middleware.json-params)
  (:require [cheshire.core :as json]
            [garbageday.model :as model]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes routes
  (GET ["/api/1.0/lg=:longitude&lt=:latitude", :longitude #"\-{0,1}\d+\.\d+", :latitude #"\-{0,1}\d+\.\d+"]
       [longitude latitude]
       (json-response {:day-of-week (model/collection-day longitude latitude)})))