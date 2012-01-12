(ns garbageday.model)

(defn garbage-day [longitude latitude]
  {:day-of-week "Monday" :lg longitude :lt latitude})