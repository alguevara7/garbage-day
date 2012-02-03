(ns garbageday.web.controllers.search
  (:use [compojure.core :only [defroutes GET POST]]
        [hiccup.page-helpers :only [url]])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [garbageday.web.views.search :as view]
            [garbageday.web.models.schedule :as model]))

(defn index [{address :address year :year month :month day :day}]
  (let [coordinates (model/geo-locate address)]
    (view/index (when-not (nil? address) (model/search (:longitude coordinates) (:latitude coordinates) year month day)))))

(defn search [{address :address year :year month :month day :day}]
  (ring/redirect (url "" {"address" address "year" year "month" month "day" day})))

(defroutes routes
  (GET  "/" {params :params} (index params))
  (POST "/" {params :params} (do (println params)
                                 (search params))))
