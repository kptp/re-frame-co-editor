(ns coop.frontend.events
  (:require
   [re-frame.core :as re-frame]
   [pneumatic-tubes.core :as tubes]
   [coop.frontend.db :as db]))

(defn on-receive [event-v]
  (re-frame/dispatch event-v))

(def tube (tubes/tube (str "ws://localhost:3449/ws") on-receive))
(def send-to-server (re-frame/after (fn [_ v] (tubes/dispatch tube v))))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   (db/default-db)))

(re-frame/reg-event-db
 ::change-name
 (fn [db [_ value]]
   (assoc db :name value)))

(re-frame/reg-event-fx
 ::init-editor
 [(re-frame/inject-cofx :co-editor)]
 (fn [{:keys [db co-editor]} _]
   {:db                        (assoc db :initialized? true)
    :initialize-connection     nil
    :editor-add-event-listener co-editor}))

(re-frame/reg-event-fx
 ::join-session
 [(re-frame/inject-cofx :co-editor)]
 (fn [{:keys [co-editor]} _]
   {:join-session co-editor}))

(re-frame/reg-event-fx
 ::init-session
 [(re-frame/inject-cofx :co-editor)]
 (fn [{:keys [co-editor]} _]
   {:init-session co-editor}))

(re-frame/reg-event-fx
 ::editor-receive-update
 [(re-frame/inject-cofx :co-editor)]
 (fn [{:keys [co-editor]} [_ value]]
   {:log                  (str "Receive: " value)
    :editor-receive-event [co-editor value]}))

(re-frame/reg-event-fx
 ::editor-send-update
 [send-to-server]
 (fn [_ [_ value]]
   {:log (str "Send: " value)}))

(re-frame/reg-event-fx
 ::master-disconnected
 (fn [_ _]
   {:log "Master disconnected"
    :alert "Master disconnected"
    :destroy-connection nil}))

(defn create-tube! []
  (tubes/create! tube))

(defn destroy-tube! []
  (tubes/destroy! tube))