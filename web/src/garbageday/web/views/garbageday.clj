(ns garbageday.web.views.garbageday
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers)
  (:require [garbageday.web.models.garbage-collection :as gc]
            [garbageday.web.models.location :as location]
            [garbageday.web.views.common :as common]
            [noir.validation :as vali]
            [clojure.string :as string]
            [noir.response :as resp]))

(defpartial error-text [errors]
  [:p (string/join "<br/>" errors)])

(defpartial search-fields [address]
  (vali/on-error :address error-text)
  (text-field {:placeholder "Address"} :address address)
  (hidden-field {:placeholder "Year"} :year)
  (hidden-field {:placeholder "Month"} :month)
  (hidden-field {:placeholder "Day"} :day))

(defpartial render-item [item]
  [:li (str item)])

(defpartial render-schedule [{:keys [collection-date items] :as schedule}]
  (when (seq schedule)
    [:ul
     [:li collection-date]
     [:li [:ul (map render-item items)]]]))

(defpartial result-page [address schedule]
  (common/main-layout
   (form-to [:post "/search"]
            (search-fields address)
            (submit-button {:class "submit"} "Find"))
   (render-schedule schedule)
   ))

;; clicking on the "Garbage Day" button submits the form to this function
(defpage [:post "/search"] {:keys [address year month day]}
  (let [{:keys [longitude latitude]} (location/geo-locate address)]
    (resp/redirect (url "/search" {:lg longitude :lt latitude :year year :month month :day day :address address}))))

(defpage "/" []
  (resp/redirect "/search"))

(defpage "/search" {longitude :lg latitude :lt address :address
                    year :year month :month day :day}
  (result-page
   address
   (when address
     (let [schedule (gc/collection-schedule longitude latitude)]
       (gc/next-collection schedule (read-string year) (+ (read-string month) 1) (read-string day))))))