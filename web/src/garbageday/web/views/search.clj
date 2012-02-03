(ns garbageday.web.views.search
  (:use [hiccup.core :only [html h]]
        [hiccup.page-helpers :only [doctype]]
        [hiccup.form-helpers :only [form-to label text-area submit-button hidden-field]])
  (:require [garbageday.web.views.layout :as layout]))

(defn search-form []
  [:div {:id "search-form" :class "sixteen columns alpha omega"}
   (form-to [:post "/"]
            (label "address" "Address of home?") 
            (text-area "address")
            (hidden-field "year")
            (hidden-field "month")
            (hidden-field "day")
            (submit-button "Search"))])

(defn display [items-collected]
  [:div {:id "shouts sixteen columns alpha omega"}
   (if-not (nil? items-collected)
     (map (fn [item] [:h2 {:class "item-collected"} (h item)]) items-collected))])

(defn index [what-is-collected]
  (layout/common "GARBAGE DAY"
                 (search-form)
                 [:div {:class "clear"}]
                 (display what-is-collected)))
