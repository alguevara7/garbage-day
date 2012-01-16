(ns garbageday.core
  (:use compojure.core)
  (:require [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [garbageday.rest]))

(defroutes urls
  garbageday.rest/routes
  (route/not-found "Page not Found!"))

(def application (handler/api urls))

(defn start [port]
  (ring/run-jetty (var application) {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (start port)))