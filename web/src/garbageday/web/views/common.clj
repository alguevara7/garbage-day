(ns garbageday.web.views.common
  (use noir.core
       hiccup.core
       hiccup.page-helpers))

;; links and includes
(def all-include-tags {:jquery (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js")
                       :base-css (include-css "css/base.css")
                       :skeleton-css (include-css "css/skeleton.css")
                       :layout-css (include-css "css/layout.css")})

(def all-javascript-tags {:set-date-function
   (javascript-tag (str "function setDate() {"
                        "  var now = new Date();"
                        "  document.getElementById('year').setAttribute('value', now.getFullYear());"
                        "  document.getElementById('month').setAttribute('value', now.getMonth());"
                        "  document.getElementById('day').setAttribute('value', now.getDate());"
                        "}"))})

;; helper partials
(defpartial build-head [include-tags javascript-tags]
  [:head
   [:title "Next Garbage Pickup - Toronto"]
   [:meta {:name "description" :content ""}]
   [:meta {:charset "utf-8"}]
   [:meta {:name "author" :content ""}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
   [:link {:type "shortcut icon" :href (resolve-uri "img/favicon.ico")}]
   [:link {:type "apple-touch-icon" :href (resolve-uri "img/apple-touch-icon.png")}]
   [:link {:type "apple-touch-icon" :sizes "72x72" :href (resolve-uri "img/apple-touch-icon-72x72.png")}]
   [:link {:type "apple-touch-icon" :sizes "114x114" :href (resolve-uri "img/apple-touch-icon-114x114.png")}]
   (map #(get all-include-tags %) include-tags)
   (map #(get all-javascript-tags %) javascript-tags)])


;; layouts
(defpartial main-layout [& content]
  (html5
   (build-head [:jquery :base-css :skeleton-css :layout-css] [:set-date-function])
   [:body {:onload "setDate()"}
    [:div.container
     [:div {:class "sixteen columns"}
      [:h3 {:class "remove-bottom" :style "margin-top: 40px"} "Next Garbage Pickup"]
      [:h6 "Find the next garbage pickup day for an address in Toronto"]]
     content]]))
