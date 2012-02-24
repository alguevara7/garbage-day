(ns garbageday.web.views.garbageday
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers)
  (:require [garbageday.web.models.schedule :as schedules]
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
  (hidden-field {:placeholder "Date"} :date))

(defpartial render-item [item]
  [:li (str item)])

(defpartial render-schedule [{:keys [collection-date items] :as schedule}]
  (when (seq schedule)
    [:ul
     [:li collection-date]
     [:li [:ul (map render-item items)]]]))

(defpartial result-page [address schedule]
  (common/main-layout
   (render-schedule schedule)
   (form-to [:post "/search"]
            (search-fields address)
            (submit-button {:class "submit"} "Find"))))

;; clicking on the "Garbage Day" button submits the form to this function
(defpage [:post "/search"] {:keys [address date]}
  (let [{:keys [longitude latitude]} (location/geo-locate address)]
    (resp/redirect (url "/search" {:lg longitude :lt latitude :date date :address address}))))

(defpage "/" []
  (resp/redirect "/search/"))

(defpage "/search" {longitude :lg latitude :lt date :date address :address}
  (result-page address {:collection-date date :items [:garbage :recycling-bin]}))