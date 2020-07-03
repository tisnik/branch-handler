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

(ns branch-handler.core-test
  (:require [clojure.test :refer :all]
            [branch-handler.core :refer :all]))

;
; Common functions used by tests.
;

(defn callable?
    "Test if given function-name is bound to the real function."
    [function-name]
    (clojure.test/function? function-name))



;
; Actual tests that checks if all functions exists.
;

(deftest test-read-request-body-existence
  "Check that the branch-handler.core/read-request-body definition exists."
  (testing "if the branch-handler.core/read-request-body definition exists."
    (is (callable? 'branch-handler.core/read-request-body))))


(deftest test-log-request-existence
  "Check that the branch-handler.core/log-request definition exists."
  (testing "if the branch-handler.core/log-request definition exists."
    (is (callable? 'branch-handler.core/log-request))))


(deftest test-send-response-existence
  "Check that the branch-handler.core/send-response definition exists."
  (testing "if the branch-handler.core/send-response definition exists."
    (is (callable? 'branch-handler.core/send-response))))


(deftest test-parse-gitlab-info-existence
  "Check that the branch-handler.core/parse-gitlab-info definition exists."
  (testing "if the branch-handler.core/parse-gitlab-info definition exists."
    (is (callable? 'branch-handler.core/parse-gitlab-info))))


(deftest test-repo-workdir-existence
  "Check that the branch-handler.core/repo-workdir definition exists."
  (testing "if the branch-handler.core/repo-workdir definition exists."
    (is (callable? 'branch-handler.core/repo-workdir))))


(deftest test-repodir-exists?-existence
  "Check that the branch-handler.core/repodir-exists? definition exists."
  (testing "if the branch-handler.core/repodir-exists? definition exists."
    (is (callable? 'branch-handler.core/repodir-exists?))))


(deftest test-fetch-mirror-repo-existence
  "Check that the branch-handler.core/fetch-mirror-repo definition exists."
  (testing "if the branch-handler.core/fetch-mirror-repo definition exists."
    (is (callable? 'branch-handler.core/fetch-mirror-repo))))


(deftest test-clone-mirror-repo-existence
  "Check that the branch-handler.core/clone-mirror-repo definition exists."
  (testing "if the branch-handler.core/clone-mirror-repo definition exists."
    (is (callable? 'branch-handler.core/clone-mirror-repo))))


(deftest test-read-branch-list-from-repo-existence
  "Check that the branch-handler.core/read-branch-list-from-repo definition exists."
  (testing "if the branch-handler.core/read-branch-list-from-repo definition exists."
    (is (callable? 'branch-handler.core/read-branch-list-from-repo))))


(deftest test-clone-or-fetch-mirror-repo-existence
  "Check that the branch-handler.core/clone-or-fetch-mirror-repo definition exists."
  (testing "if the branch-handler.core/clone-or-fetch-mirror-repo definition exists."
    (is (callable? 'branch-handler.core/clone-or-fetch-mirror-repo))))


(deftest test-start-jenkins-jobs-existence
  "Check that the branch-handler.core/start-jenkins-jobs definition exists."
  (testing "if the branch-handler.core/start-jenkins-jobs definition exists."
    (is (callable? 'branch-handler.core/start-jenkins-jobs))))


(deftest test-filter-branch-jobs-existence
  "Check that the branch-handler.core/filter-branch-jobs definition exists."
  (testing "if the branch-handler.core/filter-branch-jobs definition exists."
    (is (callable? 'branch-handler.core/filter-branch-jobs))))


(deftest test-read-jobs-for-branches-existence
  "Check that the branch-handler.core/read-jobs-for-branches definition exists."
  (testing "if the branch-handler.core/read-jobs-for-branches definition exists."
    (is (callable? 'branch-handler.core/read-jobs-for-branches))))


(deftest test-parse-job-name-existence
  "Check that the branch-handler.core/parse-job-name definition exists."
  (testing "if the branch-handler.core/parse-job-name definition exists."
    (is (callable? 'branch-handler.core/parse-job-name))))


(deftest test-parse-job-names-existence
  "Check that the branch-handler.core/parse-job-names definition exists."
  (testing "if the branch-handler.core/parse-job-names definition exists."
    (is (callable? 'branch-handler.core/parse-job-names))))


(deftest test-create-or-delete-jenkins-job-existence
  "Check that the branch-handler.core/create-or-delete-jenkins-job definition exists."
  (testing "if the branch-handler.core/create-or-delete-jenkins-job definition exists."
    (is (callable? 'branch-handler.core/create-or-delete-jenkins-job))))


(deftest test-create-action-queue-existence
  "Check that the branch-handler.core/create-action-queue definition exists."
  (testing "if the branch-handler.core/create-action-queue definition exists."
    (is (callable? 'branch-handler.core/create-action-queue))))


(deftest test-action-consumer-existence
  "Check that the branch-handler.core/action-consumer definition exists."
  (testing "if the branch-handler.core/action-consumer definition exists."
    (is (callable? 'branch-handler.core/action-consumer))))


(deftest test-start-action-consumer-existence
  "Check that the branch-handler.core/start-action-consumer definition exists."
  (testing "if the branch-handler.core/start-action-consumer definition exists."
    (is (callable? 'branch-handler.core/start-action-consumer))))


(deftest test-new-action-existence
  "Check that the branch-handler.core/new-action definition exists."
  (testing "if the branch-handler.core/new-action definition exists."
    (is (callable? 'branch-handler.core/new-action))))


(deftest test-get-operation-existence
  "Check that the branch-handler.core/get-operation definition exists."
  (testing "if the branch-handler.core/get-operation definition exists."
    (is (callable? 'branch-handler.core/get-operation))))


(deftest test-get-branch-existence
  "Check that the branch-handler.core/get-branch definition exists."
  (testing "if the branch-handler.core/get-branch definition exists."
    (is (callable? 'branch-handler.core/get-branch))))


(deftest test-api-call-handler-existence
  "Check that the branch-handler.core/api-call-handler definition exists."
  (testing "if the branch-handler.core/api-call-handler definition exists."
    (is (callable? 'branch-handler.core/api-call-handler))))


(deftest test-http-request-handler-existence
  "Check that the branch-handler.core/http-request-handler definition exists."
  (testing "if the branch-handler.core/http-request-handler definition exists."
    (is (callable? 'branch-handler.core/http-request-handler))))


(deftest test-start-server-existence
  "Check that the branch-handler.core/start-server definition exists."
  (testing "if the branch-handler.core/start-server definition exists."
    (is (callable? 'branch-handler.core/start-server))))


(deftest test-create-workdir-existence
  "Check that the branch-handler.core/create-workdir definition exists."
  (testing "if the branch-handler.core/create-workdir definition exists."
    (is (callable? 'branch-handler.core/create-workdir))))


(deftest test--main-existence
  "Check that the branch-handler.core/-main definition exists."
  (testing "if the branch-handler.core/-main definition exists."
    (is (callable? 'branch-handler.core/-main))))
