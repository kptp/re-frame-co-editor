(ns coop.backend.core
  (:use [org.httpkit.server :only [run-server]]
        [compojure.core :only [GET POST defroutes routes]]
        [compojure.handler :only [api]]
        [ring.util.response :only [file-response response]]
        [pneumatic-tubes.core :only [receiver transmitter dispatch]]
        [pneumatic-tubes.httpkit :only [websocket-handler]])
  (:require [coop.backend.master-handler :as master]
            [clojure.tools.logging :as logger]))

(def tx (transmitter))
(def dispatch-to (partial dispatch tx))

(def rx (receiver
         {:tube/on-create
          (fn [{from :tube/id} _]
            (if-not (master/master)
              (do
                (logger/info (str "Setting master to: " from))
                (dispatch-to from [:coop.frontend.events/init-session])
                (master/set-master! from))
              (do
                (logger/info (str "Connected: " from))
                (dispatch-to from [:coop.frontend.events/join-session]))))

          :tube/on-destroy
          (fn [{from :tube/id} _]
            (if (= from (master/master))
              (do
                (logger/info "Master disconnected")
                (dispatch-to :all [:coop.frontend.events/master-disconnected])
                (master/clear-master!))
              (logger/info (str "Disconnected: " from))))

          :coop.frontend.events/editor-send-update
          (fn [_ [_ update]]
            (dispatch-to :all [:coop.frontend.events/editor-receive-update update]))}))

(defroutes handler
  (GET "/ws" [] (websocket-handler rx)))

(defn -main []
  (println "Starting server")
  (run-server handler {:port 3449})
  (println "Running server"))