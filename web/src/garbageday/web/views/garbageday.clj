(ns garbageday.web.views.garbageday
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers
        clj-time.coerce
        [clojure.tools.logging :only [info]]
        [noir.options :only [dev-mode?]])
  (:require [garbageday.web.models.garbage-collection :as gc]
            [garbageday.web.models.location :as location]
            [garbageday.web.views.common :as common]
            [noir.validation :as vali]
            [clojure.string :as string]
            [noir.response :as resp]
            [clj-time.format :as dtfmt]
            [garbageday.web.models.cache :as cache]))

(def memcached-spec {:hosts (str (get (System/getenv) "MEMCACHE_SERVERS" "localhost") ":11211")
                     :username (get (System/getenv) "MEMCACHE_USERNAME" "username")
                     :password (get (System/getenv) "MEMCACHE_PASSWORD" "password")
                     :prod-mode (not (dev-mode?))})

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
  ;(info (str "POST>" year "/" month "/" day " - " address))
  (cache/with-memcached memcached-spec
    (let [collection-info (or (cache/get-value address year month day)
                              (gc/next-collection-at-address address year month day))]
      (cache/put address year month day collection-info)
      (resp/redirect (url "/" {:address address :year year :month month :day day}))))
  )

(defn log-info [message]
  (println *out*))

(defpage "/" {address :address year :year month :month day :day}
  (log-info (str "GET>" year "/" month "/" day " - " address))
  (cache/with-memcached memcached-spec
    (let [collection-info (cache/get-value address year month day)]
      (result-page address collection-info))))

;;(do (vali/rule (vali/has-value? address) [:address "There must be an address"]) (not (vali/errors? :address)))