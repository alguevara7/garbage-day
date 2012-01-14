(ns garbageday.model
  (:use clojure.contrib.java-utils)
  (:use [clojure.contrib.string :only [join]])
  (:import (java.util Date Timer Random)
           (org.geotools.data DataStoreFinder)
           (org.geotools.filter.text.cql2 CQL)))

(defn query-shape-file [longitude latitude]
  (let [url (as-url (file "data/Soilid_Waste_Datime_Curbside_Collection_Areas_WGS84/Day_areas_WGS84.shp"))
        data-store (DataStoreFinder/getDataStore {"url" url})
        day-areas-type (first (.getTypeNames data-store))
        day-areas-source (.getFeatureSource data-store day-areas-type)
        filter (CQL/toFilter (str "CONTAINS (the_geom,POINT(" longitude " " latitude "))"))
        feature (first (.getFeatures day-areas-source filter))
        day-of-week (.getAttribute feature "Schedule")
        ]
    (str day-of-week)))

(defn garbage-day [longitude latitude]
  {:day-of-week (query-shape-file longitude latitude)})


