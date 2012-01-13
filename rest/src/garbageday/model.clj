(ns garbageday.model
  (:use clojure.contrib.java-utils)
  (:import (java.util Date Timer Random)
           (org.geotools.data DataStoreFinder)))

(defn garbage-day [longitude latitude]
  (do
    (play)
    {:day-of-week "Monday" :lg longitude :lt latitude})
  )


(defn play []
  (DataStoreFinder/getDataStore {:url (as-url (file "data/Soilid_Waste_Datime_Curbside_Collection_Areas_WGS84/Day_areas_WGS84.shp"))}))