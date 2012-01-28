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

(defn week-relative-to-august-1st [year month day]
  "1 is returned for the first week"
  (+ 1 (dt/in-weeks (dt/interval (dt/date-time 2011 8 1)  (dt/date-time year month day)) )))

(defn- schedule-to-suffix [schedule]
  (re-find #"[12]{1}" schedule))

(defn- is-garbage-collection [schedule-suffix week]
  "returns :garbage if garbage is collected during week, nil otherwise"
  (cond (and (= "1" schedule-suffix) (odd? week)) :garbage
        (and (= "2" schedule-suffix) (even? week)) :garbage))

(defn- is-recycling-collection [schedule-suffix week]
  (cond (and (= "1" schedule-suffix) (even? week)) :recycling
        (and (= "2" schedule-suffix) (odd? week)) :recycling))

(defn- is-yard-waste-collection [schedule-suffix week]
  (cond (and (= "1" schedule-suffix) (odd? week) (not (some #{week} (range 20 33)))) :yard-waste
        (and (= "2" schedule-suffix) (even? week) (not (some #{week} (range 21 34)))) :yard-waste))

(defn- is-christmas-tree-collection [schedule-suffix week]
  (cond (and (= "1" schedule-suffix) (some #{week} [23 25])) :christmas-tree
        (and (= "2" schedule-suffix) (some #{week} [24 26])) :christmas-tree))

(defn schedule-to-day-of-week [schedule]
  (let [day-of-week-prefix (re-find #"[a-zA-Z]*" schedule)]
    (cond (= "Tuesday" day-of-week-prefix) 2
          (= "Wednesday" day-of-week-prefix) 3
          (= "Thursday" day-of-week-prefix) 4
          (= "Friday" day-of-week-prefix) 5)))

(defn- what-is-collected-on-week [schedule week]
  (let [schedule-suffix (schedule-to-suffix schedule)]
    (remove nil? [:green-bin
                  (is-garbage-collection schedule-suffix week)
                  (is-recycling-collection schedule-suffix week)
                  (is-yard-waste-collection schedule-suffix week)
                  (is-christmas-tree-collection schedule-suffix week)])))

(defn ymd-to-day-of-week [year month day]
  (dt/day-of-week (dt/date-time year month day)))

(defn what-is-collected [schedule year month day]
  (let [day-of-week (schedule-to-day-of-week schedule)]
    (if (= day-of-week (ymd-to-day-of-week year month day))
      (what-is-collected-on-week schedule (week-relative-to-august-1st year month day))
      [])))


