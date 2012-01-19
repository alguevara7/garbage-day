(ns garbageday.test.model
  (:use [garbageday.model])
  (:use [clojure.test]))

(deftest test-collection-day
  (is (= "Tuesday 2" (collection-day "-79.410950" "43.667957")) "my garbage day")
  (is (= "Unknown" (collection-day "0.0" "0.0")) "coordicate not in shape file"))

(deftest test-what-is-collected
  (is (= [] (what-is-collected "Tuesday 2" 2001 :august 1)))
  (is (= [:green-bin :recycling] (what-is-collected "Tuesday 2" 2001 :august 2))))