(ns pjstadig.humane-test-output
  (:use [clojure.test]
        [pjstadig.util])
  (:require [lambdaisland.deep-diff :as deep]
            [clojure.pprint :as pp]))

(defn =-body
  [msg a more]
  (if (seq more)
    `(let [a# (do ~a)
           more# (seq (list ~@more))
           result# (apply = a# more#)]
       (if result#
         (do-report {:type :pass, :message ~msg,
                     :expected a#, :actual more#})
         (do-report {:type :fail, :message ~msg,
                     :expected a#, :actual more#,
                     :diffs (map #(vector % (deep/diff a# %))
                                 more#)}))
       result#)
    `(throw (Exception. "= expects more than one argument"))))

(defonce activation-body
  (delay
   (when (not (System/getenv "INHUMANE_TEST_OUTPUT"))
     (defmethod assert-expr '= [msg [_ a & more]]
       (=-body msg a more))
     (defmethod assert-expr 'clojure.core/= [msg [_ a & more]]
       (=-body msg a more))

     (define-fail-report)
     ;; this code is just yanked from clojure.pprint
     (defmethod pp/simple-dispatch clojure.lang.IRecord [arec]
       (pprint-record arec))
     (prefer-method pp/simple-dispatch
                    clojure.lang.IRecord
                    clojure.lang.IPersistentMap))))

(defn activate! []
  @activation-body)
