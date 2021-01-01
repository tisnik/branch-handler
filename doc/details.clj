{:aliases {"downgrade" "upgrade"},
 :checkout-deps-shares
 [:source-paths
  :test-paths
  :resource-paths
  :compile-path
  "#'leiningen.core.classpath/checkout-deps-paths"],
 :clean-targets [:target-path],
 :compile-path
 "/home/ptisnovs/src/clojure/xrepos/branch-handler/target/default/classes",
 :dependencies
 ([org.clojure/clojure "1.10.1"]
  [org.clojure/tools.cli "0.3.1"]
  [ring/ring-core "1.8.2"]
  [ring/ring-jetty-adapter "1.8.2"]
  [org.clojure/data.json "0.2.5"]
  [clj-http/clj-http "2.0.0"]
  [org.clojure/tools.logging "0.3.1"]
  [log4j/log4j
   "1.2.17"
   :exclusions
   ([javax.mail/mail]
    [javax.jms/jms]
    [com.sun.jmdk/jmxtools]
    [com.sun.jmx/jmxri])]
  [org.slf4j/slf4j-log4j12 "1.6.6"]
  [org.clojars.tisnik/clj-jenkins-api "0.10.0-SNAPSHOT"]
  [org.clojars.tisnik/clj-fileutils "0.2.0-SNAPSHOT"]
  [nrepl/nrepl "0.7.0" :exclusions ([org.clojure/clojure])]
  [clojure-complete/clojure-complete
   "0.2.5"
   :exclusions
   ([org.clojure/clojure])]
  [venantius/ultra "0.6.0"]),
 :deploy-repositories
 [["clojars"
   {:url "https://repo.clojars.org/",
    :password :gpg,
    :username :gpg}]],
 :description "Git branch handler service",
 :dev-dependencies [[lein-ring "0.8.10"]],
 :eval-in :subprocess,
 :global-vars {},
 :group "branch-handler",
 :jar-exclusions ["#\"^\\.\"" "#\"\\Q/.\\E\""],
 :jvm-opts
 ["-XX:-OmitStackTraceInFastThrow"
  "-XX:+TieredCompilation"
  "-XX:TieredStopAtLevel=1"],
 :license
 {:name "Eclipse Public License",
  :url "http://www.eclipse.org/legal/epl-v10.html"},
 :main branch-handler.core,
 :monkeypatch-clojure-test false,
 :name "branch-handler",
 :native-path
 "/home/ptisnovs/src/clojure/xrepos/branch-handler/target/default/native",
 :offline? false,
 :pedantic? ranges,
 :plugin-repositories
 [["central"
   {:url "https://repo1.maven.org/maven2/", :snapshots false}]
  ["clojars" {:url "https://repo.clojars.org/"}]],
 :plugins
 ([lein-ring/lein-ring "0.12.5"]
  [lein-codox/lein-codox "0.10.7"]
  [test2junit/test2junit "1.1.0"]
  [lein-cloverage/lein-cloverage "1.0.7-SNAPSHOT"]
  [lein-kibit/lein-kibit "0.1.8"]
  [lein-clean-m2/lein-clean-m2 "0.1.2"]
  [lein-project-edn/lein-project-edn "0.3.0"]
  [lein-marginalia/lein-marginalia "0.9.1"]
  [venantius/ultra "0.6.0"]),
 :prep-tasks ["javac" "compile"],
 :profiles
 {:uberjar {:aot [:all]},
  :whidbey/repl
  {:dependencies [[mvxcvi/whidbey "RELEASE"]],
   :repl-options
   {:init
    (do
     nil
     (clojure.core/require 'whidbey.repl)
     (whidbey.repl/init! nil)),
    :custom-init (do nil (whidbey.repl/update-print-fn!)),
    :nrepl-context
    {:interactive-eval {:printer whidbey.repl/render-str}}}}},
 :project-edn {:output-file "doc/details.clj"},
 :release-tasks
 [["vcs" "assert-committed"]
  ["change" "version" "leiningen.release/bump-version" "release"]
  ["vcs" "commit"]
  ["vcs" "tag"]
  ["deploy"]
  ["change" "version" "leiningen.release/bump-version"]
  ["vcs" "commit"]
  ["vcs" "push"]],
 :repl-options
 {:init
  (do
   (do
    (clojure.core/require 'ultra.hardcore)
    (clojure.core/require 'whidbey.repl)
    (whidbey.repl/init! nil)
    (ultra.hardcore/configure!
     {:repl
      {:print-meta false,
       :map-delimiter "",
       :print-fallback :print,
       :sort-keys true}})))},
 :repositories
 [["central"
   {:url "https://repo1.maven.org/maven2/", :snapshots false}]
  ["clojars" {:url "https://repo.clojars.org/"}]],
 :resource-paths
 ("/home/ptisnovs/src/clojure/xrepos/branch-handler/dev-resources"
  "/home/ptisnovs/src/clojure/xrepos/branch-handler/resources"),
 :ring {:handler branch-handler.core/ring-app},
 :root "/home/ptisnovs/src/clojure/xrepos/branch-handler",
 :source-paths
 ("/home/ptisnovs/src/clojure/xrepos/branch-handler/src"),
 :target-path
 "/home/ptisnovs/src/clojure/xrepos/branch-handler/target/default",
 :test-paths ("/home/ptisnovs/src/clojure/xrepos/branch-handler/test"),
 :test-selectors {:default (constantly true)},
 :uberjar-exclusions ["#\"(?i)^META-INF/[^/]*\\.(SF|RSA|DSA)$\""],
 :url "https://github.com/tisnik/branch-handler",
 :version "0.1.0-SNAPSHOT"}
