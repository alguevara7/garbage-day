(defproject garbageday.web "0.1.0"
  :description ""
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/java.jdbc "0.1.3"]
                 [clj-time "0.3.7"]
                 [noir "1.3.0-alpha10"]
                 [org.geotools/gt2-shapefile "2.4.5"]
                 [org.geotools/gt2-cql "2.4.5"]
                 [clj-http "0.3.0"]
                 [spy/spymemcached "2.8.0" :exclusions [com.sun.jmx/jmxri com.sun.jdmk/jmxtools javax.jms/jms]]]
  :main garbageday.web.server
  :repositories {"spy repository" "http://files.couchbase.com/maven2"})