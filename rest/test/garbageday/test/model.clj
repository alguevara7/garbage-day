(ns garbageday.test.model
  (:use [garbageday.model])
  (:use [clojure.test]))

(deftest test-collection-schedule
  (is (= "Tuesday 2" (collection-schedule "-79.410950" "43.667957")) "my garbage day")
  (is (= "Unknown" (collection-schedule "0.0" "0.0")) "coordicate not in shape file"))

(deftest test-what-is-collected
  (is (= [] (what-is-collected "Tuesday 2" 2001 8 1)))
  (is (= [:green-bin :recycling] (what-is-collected "Tuesday 2" 2012 1 3)))
  (is (= [:green-bin :garbage] (what-is-collected "Tuesday 2" 2011 12 27)))
  (is (= [:green-bin :garbage :christmas-tree] (what-is-collected "Tuesday 2" 2012 1 10)))
  (is (= [:green-bin :garbage :yard-waste] (what-is-collected "Tuesday 2" 2012 5 1)))
  (is (= [:green-bin :garbage] (what-is-collected "Tuesday 2" 2012 2 7))))

(deftest test-is-tuesday
  (is (true? (is-tuesday 2012 1 17)))
  (is (true? (is-tuesday 2011 12 6))))

(deftest test-week-relative-to-august-1st
  (is (= 1 (week-relative-to-august-1st 2011 8 1)))
  (is (= 6 (week-relative-to-august-1st 2011 9 7)))
  (is (= 19 (week-relative-to-august-1st 2011 12 6))))