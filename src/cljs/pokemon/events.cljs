(ns pokemon.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [pokemon.db :as db]
            [pokemon.queries :as q]))

(re-frame/reg-event-fx
  ::initialize
  (fn [_ _]
    {:db db/default-db,
     :dispatch [::re-graph/query q/pokemon {:pokemon-name db/default-db}
                [::fetch-pokemon]]}))

(re-frame/reg-event-db ::update-pokemon-name
                       (fn [db [_ pokemon-name]]
                         (assoc db :pokemon-name pokemon-name)))

(re-frame/reg-event-db
  ::pokemons
  (fn [db [_ {:keys [data errors]}]]
    (if (nil? errors) (assoc db :pokemons (:pokemons data)) db)))

(re-frame/reg-event-db
  ::fetch-pokemon
  (fn [db [_ {:keys [data errors]}]]
    (if (nil? errors) (assoc db :pokemon (:pokemon data)) db)))
