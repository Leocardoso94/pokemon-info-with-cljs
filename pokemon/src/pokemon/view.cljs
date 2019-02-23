(ns pokemon.view
  (:require [re-frame.core :as rf]))

(defn show
  []
  [:h1 (str @(rf/subscribe [:pokemon]))])