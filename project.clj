(defproject shadow "0.0.1"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [thheller/shadow-cljs "2.8.35"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [compojure "1.6.1"]
                 [ring "1.7.1"]
                 [http-kit "2.3.0"]
                 [pneumatic-tubes "0.3.0"]
                 [clj-time "0.15.0"]]

  :main coop.backend.core

  :plugins [[lein-cljfmt "0.6.4"]
            [jonase/eastwood "0.3.5"]]

  :source-paths ["src/clj"
                 "src/cljs"])
