(ns pokemon.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::pokemon-name
 (fn [db]
   (:pokemon-name db)))

(re-frame/reg-sub
 ::pokemon
 (fn [db]
   (:pokemon db)))

(re-frame/reg-sub
 ::pokemons
 (fn [db]
   (:pokemons db)))