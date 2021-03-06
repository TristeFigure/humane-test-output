(defproject org.clojars.tristefigure.temp_forks/humane-test-output "0.8.5-SNAPSHOT"
  :description "Fork of humane-test-output. See: https://github.com/pjstadig/humane-test-output/pull/34."
  :url "https://github.com/TristeFigure/humane-test-output"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :test-selectors
  {:default (complement :intentionally-failing)
   :yes-i-know-the-tests-are-supposed-to-fail :intentionally-failing}
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/clojurescript "1.7.228"]
                                  [org.seleniumhq.selenium/selenium-java "2.52.0"]
                                  [com.codeborne/phantomjsdriver "1.2.1"]
                                  [org.clojure/spec.alpha "0.2.176"]
                                  [lambdaisland/deep-diff "0.0-35"]]
                   :plugins [[lein-cljsbuild "1.1.2"]]
                   :cljsbuild {:test-commands {"test" ["phantomjs" "dev-resources/test/phantom/run.js" "dev-resources/test/test.html"]}
                               :builds [{:id "test"
                                         :source-paths ["src" "test"]
                                         :compiler {:main pjstadig.run-all
                                                    :asset-path "../../target/cljsbuild/js/compiled/test/out"
                                                    :output-to "target/cljsbuild/js/compiled/humanize-test-output-test.js"
                                                    :output-dir "target/cljsbuild/js/compiled/test/out"
                                                    :source-map-timestamp true}}]}}
             :test {:injections [(require 'pjstadig.humane-test-output)
                                 (pjstadig.humane-test-output/activate!)]}})
