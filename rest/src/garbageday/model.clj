(ns garbageday.model
  (:use clojure.contrib.java-utils)
  (:use [clojure.contrib.string :only [join]])
  (:require [clj-time.core :as dt])
  (:import (java.util Date Timer Random)
           (org.geotools.data DataStoreFinder)
           (org.geotools.filter.text.cql2 CQL)))

(defn collection-schedule  [longitude latitude]
  "returns a map ..."
  (let [url (as-url (file "data/Soilid_Waste_Datime_Curbside_Collection_Areas_WGS84/Day_areas_WGS84.shp"))
        data-store (DataStoreFinder/getDataStore {"url" url})
        day-areas-type (first (.getTypeNames data-store))
        day-areas-source (.getFeatureSource data-store day-areas-type)
        filter (CQL/toFilter (str "CONTAINS (the_geom,POINT(" longitude " " latitude "))"))
        feature (first (.getFeatures day-areas-source filter))
        schedule (if-not (nil? feature)

                      (.getAttribute feature "Schedule")
                      "Unknown")]
    (str schedule)))

(defn is-tuesday [year month day]
  (= 2 (dt/day-of-week (dt/date-time year month day))))

(defn week-relative-to-august-1st [year month day]
  (+ 1 (dt/in-weeks (dt/interval (dt/date-time 2011 8 1)  (dt/date-time year month day)) )))

(defn is-garbage-collection [week]
  (if (even? week) :garbage))

(defn is-recycling-collection [week]
  (if (odd? week) :recycling))

(defn is-yard-waste-collection [week]
  (if (and (even? week) (not (some #{week} (range 21 34)))) :yard-waste))

(defn is-christmas-tree-collection [week]
  (if (some #{week} [24 26]) :christmas-tree))

;other api function next garbage day!

; generalize across years! use from august this year, or the previous year
(defn what-is-collected [schedule year month day]
  (cond
   (and (= "Tuesday 2" schedule) (is-tuesday year month day)) (let [week (week-relative-to-august-1st year month day)]
     (remove nil? [:green-bin
                 (is-garbage-collection week)
                 (is-recycling-collection week)
                 (is-yard-waste-collection week)
                 (is-christmas-tree-collection week)]))
   
   :else []))



