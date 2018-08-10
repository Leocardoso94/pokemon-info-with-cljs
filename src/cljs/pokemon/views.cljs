(ns pokemon.views
  (:require
   [re-frame.core :as re-frame]
   [re-graph.core :as re-graph]
   [pokemon.subs :as subs]
   [pokemon.events :as events]
   [pokemon.queries :as q]))

(re-frame/dispatch
 [::re-graph/init
  {:ws-url nil
   :http-url "https://graphql-pokemon.now.sh/"
   :http-parameters         {:with-credentials? false}}])

(re-frame/dispatch [::re-graph/query
                    q/pokemon-query
                    {:name "Pikachu"}
                    [::events/fetch-pokemon]])

(defn pokemon-input
  []
  (let [pokemon-name (re-frame/subscribe [::subs/pokemon-name])]
    [:div
     [:label   "Pokemon: "
      [:input {:value @pokemon-name
               :on-change #(re-frame/dispatch [::events/update-pokemon-name (-> % .-target .-value)])}]]
     [:button {:on-click #(re-frame/dispatch [::re-graph/query
                                              q/pokemon-query
                                              {:name @pokemon-name}
                                              [::events/fetch-pokemon]])} "Search"]]))

(defn avatar
  [uri]
  [:img {:src uri
         :style {:width "100px"}}])

(defn pokemon-info
  []
  (let [pokemon (re-frame/subscribe [::subs/pokemon])]
    (if (nil? @pokemon) [:h3 "no Pokémon found"]
        [:div {:style {:margin-top  "50px"}}
         [avatar (:image @pokemon)]
         [:h3 (str "name: " (:name @pokemon))]
         [:h3 (str "number: " (:number @pokemon))]])))

(defn main-panel
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 @name]
     [pokemon-input]
     [pokemon-info]]))
