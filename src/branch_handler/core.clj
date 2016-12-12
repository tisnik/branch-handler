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

(defn parse-gitlab-info
    [request]
    (let [body (read-request-body request)]
        (if body
            (json/read-str body :key-fn clojure.core/keyword))))

(def empty-SHA "0000000000000000000000000000000000000000")

(defn get-operation
    [before after]
    (if (= before empty-SHA)
        (if (= after empty-SHA)
            :unknown
            :branch-created)
        (if (= after empty-SHA)
            :branch-deleted
            :branch-updated)))

(defn get-branch
    [git-ref]
    (try
        (subs git-ref (inc (.lastIndexOf git-ref "/")))
        (catch Exception e
            nil)))

(defn api-call-handler
    [request]
    (let [gitlab-info (parse-gitlab-info request)
          repository-url   (-> gitlab-info :repository :url)
          repository-name  (-> gitlab-info :repository :name)
          repository-group (-> gitlab-info :project :namespace)
          branch           (get-branch (-> gitlab-info :ref))
          before           (-> gitlab-info :before)
          after            (-> gitlab-info :after)
          operation        (get-operation before after)]
        (if verbose
            (clojure.pprint/pprint gitlab-info))
        (log/info "repository" repository-name)
        (log/info "URL"        repository-url)
        (log/info "group"      repository-group)
        (log/info "branch"     branch)
        (log/info "before"     before)
        (log/info "after"      after)
        (log/info "operation"  operation)
        ))

        

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

