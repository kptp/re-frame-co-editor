(ns coop.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [coop.frontend.events :as events]
   [coop.frontend.views :as views]
   [coop.frontend.config :as config]
   [coop.frontend.effects]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "root")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
