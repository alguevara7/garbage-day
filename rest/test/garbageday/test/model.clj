(ns garbageday.test.model
  (:use [garbageday.model])
  (:use [clojure.test]))

(deftest test-membership
  (is (= "Tuesday" (:day-of-week (garbage-day "-79.410950" "43.667957"))) "my garbage day"))