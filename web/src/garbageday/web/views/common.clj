(ns garbageday.web.views.common
  (use noir.core
       hiccup.core
       hiccup.page-helpers))

;; links and includes
(def all-include-tags {:jquery (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js")})

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
   [:title "Garbage Day - Toronto"]
   (map #(get all-include-tags %) include-tags)
   (map #(get all-javascript-tags %) javascript-tags)])


;; layouts
(defpartial main-layout [& content]
  (html5
   (build-head [:jquery] [:set-date-function])
   [:body {:onload "setDate()"}
    [:div#wrapper
     [:div.content
      [:div#header
       [:h1 (link-to "/search/" "Garbage Day - Toronto")]]
      content]]]))
