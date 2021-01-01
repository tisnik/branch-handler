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
; Actual tests that checks if all functions really exist and are callable.
;

(deftest test-read-request-body-existence
  "Check that the branch-handler.core/read-request-body function definition exists."
  (testing "if the branch-handler.core/read-request-body function definition exists."
    (is (callable? 'branch-handler.core/read-request-body))))


(deftest test-log-request-existence
  "Check that the branch-handler.core/log-request function definition exists."
  (testing "if the branch-handler.core/log-request function definition exists."
    (is (callable? 'branch-handler.core/log-request))))


(deftest test-send-response-existence
  "Check that the branch-handler.core/send-response function definition exists."
  (testing "if the branch-handler.core/send-response function definition exists."
    (is (callable? 'branch-handler.core/send-response))))


(deftest test-parse-gitlab-info-existence
  "Check that the branch-handler.core/parse-gitlab-info function definition exists."
  (testing "if the branch-handler.core/parse-gitlab-info function definition exists."
    (is (callable? 'branch-handler.core/parse-gitlab-info))))


(deftest test-repo-workdir-existence
  "Check that the branch-handler.core/repo-workdir function definition exists."
  (testing "if the branch-handler.core/repo-workdir function definition exists."
    (is (callable? 'branch-handler.core/repo-workdir))))


(deftest test-repodir-exists?-existence
  "Check that the branch-handler.core/repodir-exists? function definition exists."
  (testing "if the branch-handler.core/repodir-exists? function definition exists."
    (is (callable? 'branch-handler.core/repodir-exists?))))


(deftest test-fetch-mirror-repo-existence
  "Check that the branch-handler.core/fetch-mirror-repo function definition exists."
  (testing "if the branch-handler.core/fetch-mirror-repo function definition exists."
    (is (callable? 'branch-handler.core/fetch-mirror-repo))))


(deftest test-clone-mirror-repo-existence
  "Check that the branch-handler.core/clone-mirror-repo function definition exists."
  (testing "if the branch-handler.core/clone-mirror-repo function definition exists."
    (is (callable? 'branch-handler.core/clone-mirror-repo))))


(deftest test-read-branch-list-from-repo-existence
  "Check that the branch-handler.core/read-branch-list-from-repo function definition exists."
  (testing "if the branch-handler.core/read-branch-list-from-repo function definition exists."
    (is (callable? 'branch-handler.core/read-branch-list-from-repo))))


(deftest test-clone-or-fetch-mirror-repo-existence
  "Check that the branch-handler.core/clone-or-fetch-mirror-repo function definition exists."
  (testing "if the branch-handler.core/clone-or-fetch-mirror-repo function definition exists."
    (is (callable? 'branch-handler.core/clone-or-fetch-mirror-repo))))


(deftest test-start-jenkins-jobs-existence
  "Check that the branch-handler.core/start-jenkins-jobs function definition exists."
  (testing "if the branch-handler.core/start-jenkins-jobs function definition exists."
    (is (callable? 'branch-handler.core/start-jenkins-jobs))))


(deftest test-filter-branch-jobs-existence
  "Check that the branch-handler.core/filter-branch-jobs function definition exists."
  (testing "if the branch-handler.core/filter-branch-jobs function definition exists."
    (is (callable? 'branch-handler.core/filter-branch-jobs))))


(deftest test-read-jobs-for-branches-existence
  "Check that the branch-handler.core/read-jobs-for-branches function definition exists."
  (testing "if the branch-handler.core/read-jobs-for-branches function definition exists."
    (is (callable? 'branch-handler.core/read-jobs-for-branches))))


(deftest test-parse-job-name-existence
  "Check that the branch-handler.core/parse-job-name function definition exists."
  (testing "if the branch-handler.core/parse-job-name function definition exists."
    (is (callable? 'branch-handler.core/parse-job-name))))


(deftest test-parse-job-names-existence
  "Check that the branch-handler.core/parse-job-names function definition exists."
  (testing "if the branch-handler.core/parse-job-names function definition exists."
    (is (callable? 'branch-handler.core/parse-job-names))))


(deftest test-create-or-delete-jenkins-job-existence
  "Check that the branch-handler.core/create-or-delete-jenkins-job function definition exists."
  (testing "if the branch-handler.core/create-or-delete-jenkins-job function definition exists."
    (is (callable? 'branch-handler.core/create-or-delete-jenkins-job))))


(deftest test-create-action-queue-existence
  "Check that the branch-handler.core/create-action-queue function definition exists."
  (testing "if the branch-handler.core/create-action-queue function definition exists."
    (is (callable? 'branch-handler.core/create-action-queue))))


(deftest test-action-consumer-existence
  "Check that the branch-handler.core/action-consumer function definition exists."
  (testing "if the branch-handler.core/action-consumer function definition exists."
    (is (callable? 'branch-handler.core/action-consumer))))


(deftest test-start-action-consumer-existence
  "Check that the branch-handler.core/start-action-consumer function definition exists."
  (testing "if the branch-handler.core/start-action-consumer function definition exists."
    (is (callable? 'branch-handler.core/start-action-consumer))))


(deftest test-new-action-existence
  "Check that the branch-handler.core/new-action function definition exists."
  (testing "if the branch-handler.core/new-action function definition exists."
    (is (callable? 'branch-handler.core/new-action))))


(deftest test-get-operation-existence
  "Check that the branch-handler.core/get-operation function definition exists."
  (testing "if the branch-handler.core/get-operation function definition exists."
    (is (callable? 'branch-handler.core/get-operation))))


(deftest test-get-branch-existence
  "Check that the branch-handler.core/get-branch function definition exists."
  (testing "if the branch-handler.core/get-branch function definition exists."
    (is (callable? 'branch-handler.core/get-branch))))


(deftest test-api-call-handler-existence
  "Check that the branch-handler.core/api-call-handler function definition exists."
  (testing "if the branch-handler.core/api-call-handler function definition exists."
    (is (callable? 'branch-handler.core/api-call-handler))))


(deftest test-http-request-handler-existence
  "Check that the branch-handler.core/http-request-handler function definition exists."
  (testing "if the branch-handler.core/http-request-handler function definition exists."
    (is (callable? 'branch-handler.core/http-request-handler))))


(deftest test-start-server-existence
  "Check that the branch-handler.core/start-server function definition exists."
  (testing "if the branch-handler.core/start-server function definition exists."
    (is (callable? 'branch-handler.core/start-server))))


(deftest test-create-workdir-existence
  "Check that the branch-handler.core/create-workdir function definition exists."
  (testing "if the branch-handler.core/create-workdir function definition exists."
    (is (callable? 'branch-handler.core/create-workdir))))


(deftest test--main-existence
  "Check that the branch-handler.core/-main function definition exists."
  (testing "if the branch-handler.core/-main function definition exists."
    (is (callable? 'branch-handler.core/-main))))
