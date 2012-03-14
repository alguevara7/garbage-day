(ns garbageday.web.date
  (:use clj-time.coerce)
  (:import [java.util Calendar]))

; use native jodatime api
(defn- date-lazy-seq [start]
  (let [calendar (Calendar/getInstance)]
    (.setTime calendar (to-date start))
    (lazy-seq (cons (from-date (.getTime  calendar)) (date-lazy-seq (do (.add calendar (Calendar/DAY_OF_MONTH) 1) (from-date (.getTime calendar))))))))

(defn date-range [start end]
      (vec (take-while (fn [date] (<= (.getTime (to-date date)) (.getTime (to-date end)))) (date-lazy-seq start))))



