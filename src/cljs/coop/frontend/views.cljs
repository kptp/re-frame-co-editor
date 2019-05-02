(ns coop.frontend.views
  (:require
   ["co-editor" :as co-editor]
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [coop.frontend.events :as events]
   [coop.frontend.subs :as subs]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        initialized? (re-frame/subscribe [::subs/initialized?])]
    [:div
     [:coop.frontend.effects/co-editor {:id        "editor"
                                        :username  @name}]
     (if (not @initialized?)
       [:div
        [:input {:value @name
                 :on-change #(re-frame/dispatch [::events/change-name (-> % .-target .-value)])}]
        [:button {:on-click #(re-frame/dispatch [::events/init-editor])} "Initialize"]])]))
