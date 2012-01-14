(ns garbageday.test.model
  (:use [garbageday.model])
  (:use [clojure.test]))

(deftest test-membership
  (is (= "Tuesday 2" (:day-of-week (garbage-day "-79.410950" "43.667957"))) "my garbage day")
  (is (= "Unknown" (:day-of-week (garbage-day "0.0" "0.0"))) "coordicate not in shape file"))