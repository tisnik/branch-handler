(ns branch-handler.core)

(require '[ring.adapter.jetty      :as jetty])
(require '[ring.middleware.params  :as http-params])
(require '[ring.util.response      :as http-response])

(require '[clojure.data.json       :as json])

(require '[clojure.tools.cli       :as cli])
(require '[clojure.tools.logging   :as log])

(def verbose true)
(def port 4000)

(def ring-app
    "Definition of a Ring-based application behaviour."
    (-> http-request-handler      ; handle all events
        http-params/wrap-params)) ; and process request parameters, of course

(defn start-server
    "Start the service on regular machine."
    [ring-app port]
    (log/info "Starting the server at the port: " port)
    (jetty/run-jetty ring-app {:port port}))

(defn -main
    "Entry point to the branch handler service server."
    [& args]
    (log/info "started app on port" port)
    (start-server ring-app port))

