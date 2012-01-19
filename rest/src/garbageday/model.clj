(ns garbageday.model
  (:use clojure.contrib.java-utils)
  (:use [clojure.contrib.string :only [join]])
  (:import (java.util Date Timer Random)
           (org.geotools.data DataStoreFinder)
           (org.geotools.filter.text.cql2 CQL)))

(defn collection-day  [longitude latitude]
  "returns a map ..."
  (let [url (as-url (file "data/Soilid_Waste_Datime_Curbside_Collection_Areas_WGS84/Day_areas_WGS84.shp"))
        data-store (DataStoreFinder/getDataStore {"url" url})
        day-areas-type (first (.getTypeNames data-store))
        day-areas-source (.getFeatureSource data-store day-areas-type)
        filter (CQL/toFilter (str "CONTAINS (the_geom,POINT(" longitude " " latitude "))"))
        feature (first (.getFeatures day-areas-source filter))
        day-of-week (if-not (nil? feature)
                      (.getAttribute feature "Schedule")
                      "Unknown")]
    (str day-of-week)))

(defn is-tuesday [year month day-of-month]
  (cond
   (= 2 day-of-month) :true
   :else nil))

;other api function next garbage day!
(defn what-is-collected [day-of-week year month day-of-month]
  (cond
   (and (= "Tuesday 2" day-of-week) (is-tuesday year month day-of-month)) (cond
                                (= month :august) (cond (even? day-of-month) [:green-bin :recycling]
                                                        :else [:green-bin :garbage :yard-waste])
                                (= month :september) (cond (odd? day-of-month) [:green-bin :recycling]
                                                           :else [:green-bin :garbage :yard-waste])
                                (= month :october) (cond (odd? day-of-month) [:green-bin :recycling]
                                                         :else [:green-bin :garbage :yard-waste])
                                (= month :november) (cond (even? day-of-month) [:green-bin :recycling]
                                                          :else [:green-bin :garbage :yard-waste])
                                (= month :december) (cond (even? day-of-month) [:green-bin :recycling]
                                                          (= day-of-month 13) [:green-bin :garbage :yard-waste]
                                                          :else [:green-bin :garbage :garbage])
                                (= month :january) (cond (odd? day-of-month) [:green-bin :recycling]
                                                         :else [:green-bin :garbage :christmas-tree])
                                
                                :else []) 
   :else []))

