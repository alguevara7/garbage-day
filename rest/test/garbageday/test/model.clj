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
  (is (= [:green-bin :garbage] (what-is-collected "Tuesday 2" 2012 2 7)))
  (is (= [] (what-is-collected "Wednesday 1" 2012 2 7)))
  (is (= [:green-bin :garbage] (what-is-collected "Wednesday 1" 2012 2 1)))
  (is (= [:green-bin :recycling] (what-is-collected "Wednesday 1" 2012 2 8)))
  (is (= [:green-bin :garbage :christmas-tree] (what-is-collected "Wednesday 1" 2012 1 4)))
  (is (= [:green-bin :garbage :christmas-tree] (what-is-collected "Wednesday 1" 2012 1 18)))
  (is (= [:green-bin :garbage :yard-waste] (what-is-collected "Wednesday 1" 2011 12 7))))

;add at least one test per collection calendar calendar!
;missing Wednesday 2, Thursday 1, Thursday 2, Wednesday 2, Friday 1, Friday 2

(deftest test-ymd-to-day-of-week
  (is (= 2 (ymd-to-day-of-week 2012 1 17)))
  (is (= 5 (ymd-to-day-of-week 2011 12 2))))

(deftest test-week-relative-to-august-1st
  (is (= 1 (week-relative-to-august-1st 2011 8 1)))
  (is (= 6 (week-relative-to-august-1st 2011 9 7)))
  (is (= 19 (week-relative-to-august-1st 2011 12 6))))