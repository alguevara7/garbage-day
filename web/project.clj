(defproject shouter "1.0.0-SNAPSHOT"
  :description "SHOUT from the webtops"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/java.jdbc "0.1.1"]
                 [clj-stacktrace "0.2.4"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [ring/ring-core "1.0.1"]
                 [ring/ring-jetty-adapter "1.0.1"]
                 [compojure "1.0.1"]
                 [hiccup "0.3.8"]
                 [clj-http "0.3.0"]
                 []]
  :main garbageday.web.main
  :ring {:handler garbageday.web.main/application})