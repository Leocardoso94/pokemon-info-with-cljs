(ns pokemon.events
  (:require
   [re-frame.core :as re-frame]
   [pokemon.db :as db]
   [re-graph.core :as re-graph]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/dispatch
 [::re-graph/init
  {:ws-url nil
   :http-url "https://graphql-pokemon.now.sh/"
   :http-parameters {:with-credentials? false}}])

(re-frame/reg-event-db
 ::update-pokemon-name
 (fn [db [_ pokemon-name]]
   (assoc db :pokemon-name pokemon-name)))

(re-frame/reg-event-db
 ::fetch-pokemon
 (fn [db [_ {:keys [data errors] :as payload}]]
   (println (:data payload))
   db))


