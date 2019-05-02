(ns coop.frontend.effects
  (:require [re-frame.core :as re-frame]
            [coop.frontend.events :as events]))

(re-frame/reg-cofx
 :co-editor
 (fn [coeffects _]
   (assoc coeffects :co-editor (.getElementById js/document "editor"))))

(re-frame/reg-fx
 :initialize-connection
 (fn []
   (events/create-tube!)))

(re-frame/reg-fx
 :destroy-connection
 (fn []
   (events/destroy-tube!)))

(re-frame/reg-fx
 :alert
 (fn [string]
   (js/alert string)))

(re-frame/reg-fx
 :init-session
 (fn [editor]
   (.initSession editor)))

(re-frame/reg-fx
 :join-session
 (fn [editor]
   (.joinSession editor)))

(re-frame/reg-fx
 :editor-add-event-listener
 (fn [editor]
   (.addEventListener editor "update" #(re-frame/dispatch [::events/editor-send-update (.-detail %)]))))

(re-frame/reg-fx
 :editor-receive-event
 (fn [[editor event]]
   (.receive editor event)))

(re-frame/reg-fx
 :log
 (fn [str]
   (println str)))