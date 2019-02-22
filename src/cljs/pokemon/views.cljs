(ns pokemon.views
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [pokemon.subs :as subs]
            [pokemon.events :as events]
            [pokemon.queries :as q]
            [pokemon.utils :as utils]))

(re-frame/dispatch [::re-graph/init
                    {:ws-url nil,
                     :http-url "https://graphql-pokemon.now.sh/",
                     :http-parameters {:with-credentials? false}}])

(re-frame/dispatch [::re-graph/query q/pokemon
                    {:name @(re-frame/subscribe [::subs/pokemon-name])}
                    [::events/fetch-pokemon]])

(defn fetch-pokemons
  [first]
  (re-frame/dispatch [::re-graph/query q/pokemons {:first first}
                      [::events/pokemons]]))

(fetch-pokemons 10)

(defn pokemon-input
  []
  (let [pokemon-name (re-frame/subscribe [::subs/pokemon-name])
        search #(re-frame/dispatch [::re-graph/query q/pokemon
                                    {:name @pokemon-name}
                                    [::events/fetch-pokemon]])]
    [:div
     [:label "Pokemon: "
      [:input
       {:value @pokemon-name,
        :on-key-down #(case (.-which %)
                       13 (search)
                       nil),
        :on-change #(re-frame/dispatch [::events/update-pokemon-name
                                        (utils/get-target-value %)])}]]
     [:button {:on-click search} "Search"]]))

(defn avatar [uri] [:img {:src uri, :style {:width "100px"}}])

(defn type-list
  [types]
  [:div [:h3 "types"]
   [:ul (map-indexed (fn [index type] [:li {:key index} type]) types)]])

(defn pokemon-info
  []
  (let [pokemon (re-frame/subscribe [::subs/pokemon])]
    (if (nil? @pokemon)
      [:h3 "no Pokémon found"]
      [:div {:style {:margin-top "50px"}} [avatar (:image @pokemon)]
       [:h3 (str "name: " (:name @pokemon))]
       [:h3 (str "number: " (:number @pokemon))]
       [type-list (:types @pokemon)]])))

(defn pokemon-list
  []
  (let [pokemons (re-frame/subscribe [::subs/pokemons])
        pokemons-in-the-view (count @pokemons)]
    [:div [:ul (for [{:keys [id name]} @pokemons] [:li {:key id} name])]
     (when (> 151 pokemons-in-the-view)
       [:button {:on-click #(fetch-pokemons (+ 10 pokemons-in-the-view))}
        "Show more"])]))

(defn main-panel
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div [:h1 {:style {:text-align "center"}} @name]
     [:div
      {:style
         {:display "flex", :justify-content "space-around", :flex-wrap "wrap"}}
      [:div [:h3 "List of pokémons available:"] [pokemon-list]]
      [:div [:h2 "Get infos about a pokémon"] [pokemon-input]
       [pokemon-info]]]]))
