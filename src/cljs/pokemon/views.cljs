(ns pokemon.views
  (:require
   [re-frame.core :as re-frame]
   [re-graph.core :as re-graph]
   [pokemon.subs :as subs]
   [pokemon.events :as events]
   [pokemon.queries :as q]))

(println q/pokemon-query)

(defn pokemon-input []
  (let [pokemon-name (re-frame/subscribe [::subs/pokemon-name])]
    [:div
     [:label   "Pokemon: "
      [:input {:value @pokemon-name
               :on-change #(re-frame/dispatch [::events/update-pokemon-name (-> % .-target .-value)])}]]
     [:button {:on-click #(re-frame/dispatch [::re-graph/query
                                              q/pokemon-query
                                              {:name @pokemon-name}
                                              [::events/fetch-pokemon]])} "Buscar"]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 @name]
     [pokemon-input]]))
