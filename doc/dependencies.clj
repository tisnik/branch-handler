{[clj-http "2.0.0"]
 {[commons-codec "1.10" :exclusions [[org.clojure/clojure]]] nil,
  [org.apache.httpcomponents/httpclient
   "4.5"
   :exclusions
   [[org.clojure/clojure]]]
  {[commons-logging "1.2"] nil},
  [org.apache.httpcomponents/httpcore
   "4.4.1"
   :exclusions
   [[org.clojure/clojure]]]
  nil,
  [org.apache.httpcomponents/httpmime
   "4.5"
   :exclusions
   [[org.clojure/clojure]]]
  nil,
  [potemkin "0.4.1" :exclusions [[org.clojure/clojure]]]
  {[clj-tuple "0.2.2"] nil, [riddley "0.1.10"] nil},
  [slingshot "0.12.2" :exclusions [[org.clojure/clojure]]] nil},
 [clojure-complete "0.2.5" :exclusions [[org.clojure/clojure]]] nil,
 [log4j
  "1.2.17"
  :exclusions
  [[javax.mail/mail]
   [javax.jms/jms]
   [com.sun.jmdk/jmxtools]
   [com.sun.jmx/jmxri]]]
 nil,
 [nrepl "0.7.0" :exclusions [[org.clojure/clojure]]] nil,
 [org.clojars.tisnik/clj-fileutils "0.2.0-20160818.182300-1"]
 {[clj-rm-rf "1.0.0-20100821.141701-3"] nil},
 [org.clojars.tisnik/clj-jenkins-api "0.10.0-20160819.170845-1"] nil,
 [org.clojure/clojure "1.10.1"]
 {[org.clojure/core.specs.alpha "0.2.44"] nil,
  [org.clojure/spec.alpha "0.2.176"] nil},
 [org.clojure/data.json "0.2.5"] nil,
 [org.clojure/tools.cli "0.3.1"] nil,
 [org.clojure/tools.logging "0.3.1"] nil,
 [org.slf4j/slf4j-log4j12 "1.6.6"] {[org.slf4j/slf4j-api "1.6.6"] nil},
 [ring/ring-core "1.8.2"]
 {[commons-fileupload "1.4"] nil,
  [commons-io "2.6"] nil,
  [crypto-equality "1.0.0"] nil,
  [crypto-random "1.2.0"] nil,
  [ring/ring-codec "1.1.2"] nil},
 [ring/ring-jetty-adapter "1.8.2"]
 {[org.eclipse.jetty/jetty-server "9.4.31.v20200723"]
  {[javax.servlet/javax.servlet-api "3.1.0"] nil,
   [org.eclipse.jetty/jetty-http "9.4.31.v20200723"]
   {[org.eclipse.jetty/jetty-util "9.4.31.v20200723"] nil},
   [org.eclipse.jetty/jetty-io "9.4.31.v20200723"] nil},
  [ring/ring-servlet "1.8.2"] nil},
 [venantius/ultra "0.6.0"]
 {[grimradical/clj-semver "0.3.0" :exclusions [[org.clojure/clojure]]]
  nil,
  [io.aviso/pretty "0.1.35"] nil,
  [mvxcvi/puget "1.1.0"]
  {[fipp "0.6.14"] {[org.clojure/core.rrb-vector "0.0.13"] nil},
   [mvxcvi/arrangement "1.1.1"] nil},
  [mvxcvi/whidbey "2.1.0"] {[org.clojure/data.codec "0.1.1"] nil},
  [org.clojars.brenton/google-diff-match-patch "0.1"] nil,
  [robert/hooke "1.3.0"] nil,
  [venantius/glow "0.1.5" :exclusions [[hiccup] [garden]]]
  {[clj-antlr "0.2.3"]
   {[org.antlr/antlr4-runtime "4.5.3"] nil,
    [org.antlr/antlr4 "4.5.3"] nil},
   [instaparse "1.4.1"] nil}}}
