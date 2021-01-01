;
;  (C) Copyright 2016, 2020, 2021  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(ns branch-handler.core)

(require '[ring.adapter.jetty          :as jetty])
(require '[ring.middleware.params      :as http-params])
(require '[ring.util.response          :as http-response])

(require '[clojure.string              :as string])
(require '[clojure.java.shell          :as shell])
(require '[clojure.data.json           :as json])

(require '[clojure.tools.cli           :as cli])
(require '[clojure.tools.logging       :as log])

(require '[clj-fileutils.fileutils     :as file-utils])
(require '[clj-jenkins-api.jenkins-api :as jenkins-api])

(def verbose
  "Flag to enable or disable verbosity level of log files."
  false)

(def port
  "Default port for the service."
  3000)

(def workdir
  "Working directory used for fetching repositories."
  "/tmp/bare-repositories")

(def job-list-command
  "Command used to retrieve job list from Jenkins CI."
  "/api/json?tree=jobs[name]")

(def branch-job-prefix
  "Prefix used for all 'branch' build jobs."
  "branch-")

(def jenkins-url
  "URL to Jenkins CI server."
  (System/getenv "JENKINS_URL"))

(defn read-request-body
  "Read all informations from the request body."
  [request]
  (file-utils/slurp- (:body request)))

(defn log-request
  "Record an event about request into log."
  [request]
  (log/info "Handling request: " (:uri request) (:remote-addr request))
  (if verbose
    (clojure.pprint/pprint request)))

(defn send-response
  "Send response in HTML format to client."
  [response-text]
  (-> (http-response/response response-text)
      (http-response/content-type "text/html")))

(defn parse-gitlab-info
  "Parse payload sent from GitLab in response body."
  [request]
  (let [body (read-request-body request)]
    (if body
      (json/read-str body :key-fn clojure.core/keyword))))

(defn repo-workdir
  "Construct name of work directory for given group and repository name."
  [workdir group reponame]
  (string/join "/" [workdir group reponame]))

(defn repodir-exists?
  "Check if directory exist for given group and repository name."
  [workdir group reponame]
  (let [f (new java.io.File (repo-workdir workdir group reponame))]
    (.isDirectory f)))

(defn fetch-mirror-repo
  "Fetch the content of selected repository."
  [workdir group reponame repository-url]
  (log/info "Fetching mirror repository" repository-url)
  ; TODO test --prune
  (shell/sh "git" "--git-dir" (repo-workdir workdir group reponame) "fetch"))

(defn clone-mirror-repo
  "Clone content of selected repository."
  [workdir group reponame repository-url]
  (log/info "Cloning mirror repository" repository-url)
  (shell/sh "git"
            "clone"
            "--mirror"
            repository-url
            (repo-workdir workdir group reponame)))

(defn read-branch-list-from-repo
  "Read list of all branches from selected repository."
  [workdir group reponame]
  (let [repodir (repo-workdir workdir group reponame)]
    (log/info "Reading branches from repository" repodir)
    (->> (shell/sh "git"
                   "--git-dir"
                   (repo-workdir workdir group reponame)
                   "branch")
         :out
         string/split-lines
         (map #(subs % 2)))))

(defn clone-or-fetch-mirror-repo
  "Clone the repository or fetch data if directory with clone already exist."
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
  "Call Jenkins to start selected job."
  [jenkins-url action]
  (let [original-data (:original-data action)]
    (clojure.pprint/pprint original-data)))

(defn filter-branch-jobs
  "Filter jobs according to given prefix in job name."
  [all-jobs prefix]
  (for [job all-jobs
        :when (.startsWith (get job "name") prefix)]
    (get job "name")))

(defn read-jobs-for-branches
  "Read list of all jobs having specified prefix in job name."
  [jenkins-url job-list-command branch-job-prefix]
  (-> (jenkins-api/read-list-of-all-jobs jenkins-url job-list-command)
      (filter-branch-jobs branch-job-prefix)))

(defn parse-job-name
  "Parse the job name, try to get group, repository, and branch from it."
  [branch-job-prefix job-name]
  (let [parsed (clojure.string/split job-name #"\+")]
    (if (= (count parsed) 3)
      {:name       job-name,
       :group      (subs (clojure.string/trim (first parsed))
                         (count branch-job-prefix)),
       :repository (clojure.string/trim (second parsed)),
       :branch     (clojure.string/trim (nth parsed 2))})))

(defn parse-job-names
  "Parse all job names, try to get group, repository, and branch from them."
  [branch-job-prefix job-names]
  (for [job-name job-names]
       (parse-job-name branch-job-prefix job-name)))

(defn create-or-delete-jenkins-job
  "Create or delete Jenkins job according to selected action."
  [jenkins-url job-list-command branch-job-prefix action]
  (log/info "Create-or-delete-jenkins-job")
  (let [reponame       (:name action)
        group          (:group action)
        jobs-for-branches (read-jobs-for-branches jenkins-url
                                                  job-list-command
                                                  branch-job-prefix)
        jobs-info      (parse-job-names branch-job-prefix jobs-for-branches)
        branch-list-in-repo (read-branch-list-from-repo workdir group reponame)]
    jobs-info))

(defn create-action-queue
  "Construct queue to store all actions that needs to be performed."
  []
  (java.util.concurrent.LinkedBlockingQueue.))

(defn action-consumer
  "Consumer that process messages dequed from action queue."
  [actions-queue]
  (while true
         (when-let [action (.take actions-queue)] ; blocking operation
           (log/info "Started action for repository" action)
           (clone-or-fetch-mirror-repo action)
           (create-or-delete-jenkins-job jenkins-url
                                         job-list-command
                                         branch-job-prefix
                                         action)
           (start-jenkins-jobs jenkins-url action)
           (log/info "Finished action for repository" action))))

(defn start-action-consumer
  "Start consumer of action queue in its own thread."
  [actions-queue]
  (log/info "Starting actions consumer")
  (future                                    ; start consumer in its own thread
    (action-consumer actions-queue)))

(defn new-action
  "Plan for a new action via action queue."
  [actions-queue action]
  (.put actions-queue action))

(def empty-SHA
  "Default SHA used to detect operation that needs to be performed."
  "0000000000000000000000000000000000000000")

(defn get-operation
  "Get the next operation that needs to be planned."
  [before after]
  (if (= before empty-SHA)
    (if (= after empty-SHA)
        :unknown
        :branch-created)
    (if (= after empty-SHA)
        :branch-deleted
        :branch-updated)))

(defn get-branch
  "Retrieve branch name for given Git reference."
  [git-ref]
  (try (subs git-ref (inc (.lastIndexOf git-ref "/")))
       (catch Exception e
              nil)))

(def actions-queue
  "Queue to plan actions that will need to be performed."
  (create-action-queue))

(defn api-call-handler
  "Handler for all API calls."
  [request]
  (let [gitlab-info      (parse-gitlab-info request)
        repository-url   (-> gitlab-info :repository :url)
        repository-name  (-> gitlab-info :repository :name)
        repository-group (-> gitlab-info :project :namespace)
        branch           (get-branch (:ref gitlab-info))
        before           (:before gitlab-info)
        after            (:after gitlab-info)
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
      (let [action {:action :clone-or-fetch-repository,
                    :name    repository-name,
                    :group   repository-group,
                    :url     repository-url,
                    :original-data gitlab-info}]
        (new-action actions-queue action))) ; add new action into the queue that
                                            ; will serialize it
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
  ; handle all events
  ; and process request parameters, of course
  (http-params/wrap-params http-request-handler))

(defn start-server
  "Start the service on regular machine."
  [ring-app port]
  (log/info "Starting the server at the port: " port)
  (jetty/run-jetty ring-app {:port port}))

(defn create-workdir
  "Create new working directory to be used to fetch repository."
  [workdir]
  (let [directory (new java.io.File workdir)]
    (when-not (.isDirectory directory))
      (log/info "Creating work directory" (.getAbsolutePath directory))
      (.mkdir directory)))

(defn -main
  "Entry point to the branch handler service server."
  [& args]
  (log/info "Started app on port" port)
  (log/info "JENKINS_URL is set to" jenkins-url)
  (create-workdir workdir)
  (start-action-consumer actions-queue)
  (start-server ring-app port))

