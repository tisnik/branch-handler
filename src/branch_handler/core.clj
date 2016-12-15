(ns branch-handler.core)

(require '[ring.adapter.jetty      :as jetty])
(require '[ring.middleware.params  :as http-params])
(require '[ring.util.response      :as http-response])

(require '[clojure.data.json       :as json])

(require '[clojure.tools.cli       :as cli])
(require '[clojure.tools.logging   :as log])

(require '[clj-fileutils.fileutils :as file-utils])
(require '[clj-jenkins-api.jenkins-api :as jenkins-api])

(def verbose false)
(def port    3000)
(def workdir "/tmp/bare-repositories")
(def job-list-command "/api/json?tree=jobs[name]")
(def branch-job-prefix "branch-")


(def jenkins-url (System/getenv "JENKINS_URL"))

(defn read-request-body
    "Read all informations from the request body."
    [request]
    (file-utils/slurp- (:body request)))

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

(defn exec
    "Execute external command and pass given number of parameters to it."
    ; command called without parameters
    ([command]
     (doto (. (Runtime/getRuntime) exec command)
           (.waitFor)
           (.destroy)))
    ; command called with one or more parameters
    ([command & rest-parameters]
     (doto (. (Runtime/getRuntime) exec (str command " " (clojure.string/join " " rest-parameters)))
           (.waitFor)
           (.destroy))))

(defn repo-workdir
    [workdir group reponame]
    (clojure.string/join "/" [workdir group reponame]))

(defn repodir-exists?
    [workdir group reponame]
    (let [f (new java.io.File (repo-workdir workdir group reponame))]
        (.isDirectory f)))

(defn fetch-mirror-repo
    [workdir group reponame repository-url]
    (log/info "Fetching mirror repository" repository-url)
    ; TODO test --prune
    (clojure.java.shell/sh "git" "--git-dir" (repo-workdir workdir group reponame) "fetch"))

(defn clone-mirror-repo
    [workdir group reponame repository-url]
    (log/info "Cloning mirror repository" repository-url)
    (clojure.java.shell/sh "git" "clone" "--mirror" repository-url (repo-workdir workdir group reponame)))

(defn read-branch-list-from-repo
    [workdir group reponame]
    (let [repodir (repo-workdir workdir group reponame)]
        (log/info "Reading branches from repository" repodir)
        (->> (clojure.java.shell/sh "git" "--git-dir" (repo-workdir workdir group reponame) "branch")
             :out
             clojure.string/split-lines
             (map #(subs % 2)))))

(defn clone-or-fetch-mirror-repo
    [action]
    (log/info "Cloning or fetching repository")
    (let [reponame       (:name action)
          group          (:group action)
          repository-url (:url action)]
          (log/info (repo-workdir workdir group reponame))
          (if (repodir-exists? workdir group reponame)
              (fetch-mirror-repo workdir group reponame repository-url)
              (clone-mirror-repo workdir group reponame repository-url))))

(defn start-jenkins-jobs
    [jenkins-url action]
    (let [original-data (:original-data action)]
        (clojure.pprint/pprint original-data)
        )
)

(defn filter-branch-jobs
    [all-jobs prefix]
    (for [job all-jobs :when (.startsWith (get job "name") prefix)]
        (get job "name")))

(defn read-jobs-for-branches
    [jenkins-url job-list-command branch-job-prefix]
    (-> (jenkins-api/read-list-of-all-jobs jenkins-url job-list-command)
        (filter-branch-jobs branch-job-prefix)))

(defn create-or-delete-jenkins-job
    [jenkins-url job-list-command branch-job-prefix action]
    (log/info "Create-or-delete-jenkins-job")
    (let [reponame       (:name action)
          group          (:group action)
          jobs-for-branches (read-jobs-for-branches jenkins-url job-list-command branch-job-prefix)
          branch-list-in-repo (read-branch-list-from-repo workdir group reponame)]
          branch-list-in-repo))

(defn create-action-queue
    []
    (java.util.concurrent.LinkedBlockingQueue.))

(defn action-consumer
    [actions-queue]
    (while true
        (when-let [action (.take actions-queue)] ; blocking operation
            (log/info "Started action for repository" action)
            (clone-or-fetch-mirror-repo action)
            (create-or-delete-jenkins-job jenkins-url job-list-command branch-job-prefix action)
            (start-jenkins-jobs jenkins-url action)
            (log/info "Finished action for repository" action)
        )))

(defn start-action-consumer
    [actions-queue]
    (log/info "Starting actions consumer")
    (future                                    ; start consumer in its own thread
        (action-consumer actions-queue)))

(defn new-action
    [actions-queue action]
    (.put actions-queue action))

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

(def actions-queue (create-action-queue))

(defn api-call-handler
    [request]
    (let [gitlab-info      (parse-gitlab-info request)
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
        (if (contains? #{:branch-created :branch-deleted :branch-updated} operation)
            (let [action {:action :clone-or-fetch-repository
                          :name  repository-name
                          :group repository-group
                          :url   repository-url
                          :original-data gitlab-info}]
                 (new-action actions-queue action))) ; add new action into the queue that will serialize it
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

(defn create-workdir
    [workdir]
    (let [directory (new java.io.File workdir)]
        (when (not (.isDirectory directory))
              (log/info "Creating work directory" (.getAbsolutePath directory))
              (.mkdir directory))))

(defn -main
    "Entry point to the branch handler service server."
    [& args]
    (log/info "Started app on port" port)
    (log/info "JENKINS_URL is set to" jenkins-url)
    (create-workdir workdir)
    (start-action-consumer actions-queue)
    (start-server ring-app port))

