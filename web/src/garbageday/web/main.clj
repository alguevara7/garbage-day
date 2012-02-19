(ns garbageday.web.main
  (:use [compojure.core :only [defroutes]]
        [ring.middleware.reload]
        [ring.middleware.stacktrace])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as ring]
            [garbageday.web.controllers.search]
            [garbageday.web.views.layout :as layout]))

(defroutes routes
  garbageday.web.controllers.search/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def application (handler/site routes))

(defn start [port]
  (ring/run-jetty (wrap-stacktrace-web (wrap-reload #'application)) {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (start port)))
 
