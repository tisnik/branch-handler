;
;  (C) Copyright 2016, 2020  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(defproject branch-handler "0.1.0-SNAPSHOT"
  :description "Git branch handler service"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License",
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.3.1"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "2.0.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [org.slf4j/slf4j-log4j12 "1.6.6"]
                 [org.clojars.tisnik/clj-jenkins-api "0.10.0-SNAPSHOT"]
                 [org.clojars.tisnik/clj-fileutils "0.2.0-SNAPSHOT"]]
  :dev-dependencies [[lein-ring "0.8.10"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-codox "0.10.7"]
            [test2junit "1.1.0"]
            [lein-test-out "0.3.1"]
            [lein-cloverage "1.0.7-SNAPSHOT"]
            [lein-kibit "0.1.8"]]
  :main ^:skip-aot branch-handler.core
  :ring {:handler branch-handler.core/ring-app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

