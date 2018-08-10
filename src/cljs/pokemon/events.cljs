(ns pokemon.events
  (:require
   [re-frame.core :as re-frame]
   [pokemon.db :as db]
   [re-graph.core :as re-graph]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::update-pokemon-name
 (fn [db [_ pokemon-name]]
   (assoc db :pokemon-name pokemon-name)))

(re-frame/reg-event-db
 ::fetch-pokemon
 (fn [db [_ {:keys [data errors] :as payload}]]
   (if (nil? errors)
     (assoc db :pokemon (:pokemon data))
     db)))


