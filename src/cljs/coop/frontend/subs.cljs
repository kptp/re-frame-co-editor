(ns coop.frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::initialized?
 (fn [db _]
   (:initialized? db)))

(re-frame/reg-sub
 ::name
 (fn [db _]
   (:name db)))