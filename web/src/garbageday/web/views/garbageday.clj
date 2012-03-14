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
            [noir.response :as resp]
            [clj-time.format :as dtfmt]))

(defpartial error-text [errors]
  [:p (string/join "<br/>" errors)])

(defpartial search-fields [address]
  (vali/on-error :address error-text)
  (text-field {:placeholder "Address" :class "remove-bottom" :style "width:100%"} :address address)
  (hidden-field {:placeholder "Year"} :year)
  (hidden-field {:placeholder "Month"} :month)
  (hidden-field {:placeholder "Day"} :day))

(defpartial render-item [item]
  (image (str "img/" (name item) ".png"))) 

(defpartial render-collection-info [{:keys [date items schedule] :as collection-info}]
  (when (seq collection-info)
    [:div
     [:h5 (dtfmt/unparse (dtfmt/formatter "EEEE, MMMMM d") date)]
     [:div (map render-item items)]
     (link-to (str "http://www.toronto.ca/garbage/single/calendars/" (string/lower-case (string/replace schedule " " "_")) ".pdf")  "calendar")]))

(defpartial result-page [address collection-info]
  (common/main-layout
   [:div {:class "row remove-bottom"}
    (form-to [:post "/search"]
             [:div {:class "six columns"} (search-fields address)]
             [:div {:class "one column"} (submit-button "Find")]
             [:div {:class "nine columns"}])]
   [:div {:class "row"}
    [:div {:class "six columns" :style "text-align:left"} (render-collection-info collection-info)]]))

;;
;; clicking on the "Garbage Day" button submits the form to this function
(defpage [:post "/search"] {:keys [address year month day]}
  (let [collection-info (gc/next-collection-at-address address year month day)]
    (cache/put address year month day collection-info)
    (resp/redirect (url "/search" {:address address :year year :month month :day day}))))

(defpage "/" []
  (resp/redirect "/search"))

(defpage "/search" {address :address year :year month :month day :day}
  (let [collection-info (cache/get address year month day)]
    (result-page address collection-info)))

;;(do (vali/rule (vali/has-value? address) [:address "There must be an address"]) (not (vali/errors? :address)))