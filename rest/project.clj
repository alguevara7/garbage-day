(defproject garbageday "0.0.1-SNAPSHOT"
  :description "rest backend of Garbage Day"
  :dev-dependencies [
                     ;; allow for local server to be started
                     ;; using lein ring server (don't need to
                     ;; restart on change)
                     [lein-ring "0.4.5"]]
  :dependencies [
                 [org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [clj-stacktrace "0.2.4"]
                 [compojure "0.6.5"]
                 [ring/ring-core "0.3.8"]
                 [ring/ring-jetty-adapter "0.3.8"]
                 [ring-json-params "0.1.3"]
                 [cheshire "2.0.4"]]

    :ring {:handler garbageday.core/run-server})



;[org.clojure/clojure "1.3.0"]
;[ring/ring-jetty-adapter "1.0.0-RC1"]
;[compojure "1.0.1"]
;
;
;