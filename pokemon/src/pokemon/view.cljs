(ns pokemon.view
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent.core :as r]
            ["react-loader-spinner" :default spinner]))

(defn- loader
  [loading?]
  (when loading?
    [:div {:style {:height "100%"
                   :width "100%"
                   :display "flex"
                   :flex-direction "column"
                   :align-items "center"
                   :justify-content "center"
                   :position "absolute"
                   :background-color "rgba(0,0,0,0.5)"}}

     [:>  spinner {:type "Puff"
                   :color "#61dafb"
                   :width "100"
                   :height "100"}]]))

(def ^:private btn-style
  {:background-color "#fff"
   :border-radius 100
   :display "block"
   :margin "20px auto"
   :font-weight "500"
   :font-family "inherit"
   :border-color "transparent"
   :cursor "pointer"
   :padding "10px 15px"})

(defn- pokemon-link
  [name]
  [:a
   {:on-click #(rf/dispatch [:get-pokemon name])
    :style {:color "#61dafb"
            :text-decoration "underline"
            :cursor "pointer"}} name])

(defn- pokemon-list
  [pokemons]
  [:div {:style {:width "50%"}}
   [:p "List of Pokémon..."]
   [:ul
    (for [{:keys [id name] :as pokemon} pokemons]
      [:li {:key id}
       [pokemon-link name]])]])

(defn- evolutions-list
  [evolutions]
  (if (zero? (count evolutions))
    [:p "No evolutions"]
    [:div
     [:p {:style {:margin-bottom 0}} "Evolutions"]
     [:ul {:style {:margin-top 5}}
      (map (fn [{:keys [name]}]
             [:li {:key [name]} [pokemon-link name]]) evolutions)]]))

(defn- selected-pokemon
  [{:keys [image name number types evolutions] :as pokemon}]
  (when image
    [:div {:style {:width "50%"}}
     [:h4 {:style {:text-align "center"}}
      (str name " #" number)]
     [:img {:src image
            :style {:border-radius "50%"
                    :margin "0 auto"
                    :display "block"
                    :height "100px"}}]
     [:p "Types: " (str/join ", " types)]

     [evolutions-list evolutions]
     [:button {:on-click #(rf/dispatch [:reset-pokemon])
               :style (merge
                       btn-style
                       {:color "#fff"
                        :background-color "tomato"})}
      "Remove"]]))

(defn- title
  []
  [:h2 "Pokémons"])

(defn show
  []
  (let [pokemons (rf/subscribe [:pokemons])
        pokemon (rf/subscribe [:pokemon])]
    [:div {:style {:background-color "#282c34"
                   :min-height "100vh"
                   :display "flex"
                   :flex-direction "column"
                   :align-items "center"
                   :justify-content "center"
                   :font-size "calc(10px + 2vmin)"
                   :color "white"}}
     [loader @(rf/subscribe [:loading?])]
     [title]
     [:div {:style {:display "flex"
                    :width "100%"
                    :justify-content "space-between"}}
      [pokemon-list @pokemons]
      [selected-pokemon @pokemon]]
     (when-not (zero? (count @pokemons))
       [:button
        {:on-click #(rf/dispatch [:get-list-of-pokemons])
         :style btn-style}
        "Load more..."])]))