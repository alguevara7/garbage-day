(defproject garbageday "0.0.1-SNAPSHOT"
  :description "rest backend of Garbage Day"
  :dev-dependencies [
                     ;; allow for local server to be started
                     ;; using lein ring server (don't need to
                     ;; restart on change)
                     [lein-ring "0.5.4"]]
  :dependencies [
                 [org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [clj-stacktrace "0.2.4"]
                 [compojure "1.0.1"]
                 [ring/ring-core "1.0.1"]
                 [ring/ring-jetty-adapter "1.0.1"]
                 [ring-json-params "0.1.3"]
                 [cheshire "2.0.4"]
                 [org.geotools/gt2-shapefile "2.4.5"]
                 [org.geotools/gt2-cql "2.4.5"]
                 [clj-time "0.3.4"]]

  :ring {:handler garbageday.core/run-server}
  :repositories {"releases" "http://download.osgeo.org/webdav/geotools/"})
