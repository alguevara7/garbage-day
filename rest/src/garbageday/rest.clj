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
  (GET ["/api/1.0/lg=:longitude&lt=:latitude&y=:year&m=:month&d=:day",
        :longitude #"\-{0,1}\d+\.\d+"
        :latitude #"\-{0,1}\d+\.\d+"
        :year #"\d+"
        :month #"\d+"
        :day #"\d+"]
       [longitude latitude year month day]
       (let [schedule (model/collection-schedule longitude latitude)]
         (json-response {:day-of-week (model/as-day-of-week schedule)
                         :what-is-collected (model/what-is-collected schedule (read-string year) (read-string month) (read-string day))}))))