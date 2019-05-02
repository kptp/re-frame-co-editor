(ns coop.backend.master-handler)

(def ^:private state (atom {}))

(defn master []
  (:master @state))

(defn set-master! [uuid]
  (swap! state assoc :master uuid))

(defn clear-master! []
  (swap! state dissoc :master))
