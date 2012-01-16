(ns garbageday.test.model
  (:use [garbageday.model])
  (:use [clojure.test]))

(deftest test-membership
  (is (= "Tuesday" (query-shape-file "-79.410950" "43.667957")) "my garbage day")
  (is (= "Unknown" (query-shape-file "0.0" "0.0")) "coordicate not in shape file"))