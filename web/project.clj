
; TODO: add clojure contib as dev dependency
(defproject garbageday.web "0.1.0"
  :description ""
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/java.jdbc "0.1.3"]
                 [org.clojure/tools.logging "0.2.3"]
                 [clj-time "0.3.7"]
                 [noir "1.3.0-alpha10"]
                 [org.geotools/gt2-shapefile "2.4.5"]
                 [org.geotools/gt2-cql "2.4.5"]
                 [clj-http "0.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
		 [spy/spymemcached "2.8.0" :exclusions [com.sun.jmx/jmxri com.sun.jdmk/jmxtools javax.jms/jms]]]
  :plugins [[lein-swank "1.4.4"]]
  :main garbageday.web.server
  :repositories {"spy repository" "http://files.couchbase.com/maven2"
                 "geotools" "http://download.osgeo.org/webdav/geotools/"})
