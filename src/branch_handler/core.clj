(ns branch-handler.core)

(require '[ring.adapter.jetty      :as jetty])
(require '[ring.middleware.params  :as http-params])
(require '[ring.util.response      :as http-response])

(require '[clojure.data.json       :as json])

(require '[clojure.tools.cli       :as cli])
(require '[clojure.tools.logging   :as log])

(def verbose true)
(def port 4000)

(defn slurp-
    [filename]
    (try
        (slurp filename)
        (catch Exception e
            (log/error e)
            nil)))

(defn read-request-body
    "Read all informations from the request body."
    [request]
    (slurp- (:body request)))

(defn log-request
    [request]
    (log/info "Handling request: " (:uri request) (:remote-addr request))
    (if verbose
        (clojure.pprint/pprint request)))

(defn send-response
    [response-text]
    (-> (http-response/response response-text)
        (http-response/content-type "text/html")))

(defn http-request-handler
    "Handler that is called by Ring for all requests received from user(s)."
    [request]
    (log-request request)
    (if (= [:post "/"] [(:request-method request) (:uri request)])
        (api-call-handler request))
    (send-response ""))

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

