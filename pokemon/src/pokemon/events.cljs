(ns pokemon.events
  (:require
   [clojure.walk :as walk]
   [pokemon.queries :as q]
   [re-frame.core :as rf]))

(defn- get-json
  [e]
  (if-not (.-ok e)
    (throw (js/Error (.-statusText e)))
    (.json e)))

(rf/reg-fx :fetch
           (fn [{:keys [query variables on-error on-success]}]
             (let [body (-> {:query query
                             :variables variables} clj->js js/JSON.stringify)]
               (-> (js/fetch "https://graphql-pokemon.now.sh/"
                             (clj->js {:method      "POST"
                                       :headers {"Content-Type" "application/json"}
                                       :body        body}))
                   (.then get-json)
                   (.then (fn [e]
                            (let [{:keys [data errors]} (-> e
                                                            js->clj
                                                            walk/keywordize-keys)]
                              (if errors
                                (rf/dispatch (conj on-error errors))
                                (rf/dispatch (conj on-success data))))))
                   (.catch (fn [e]
                             (rf/dispatch (conj on-error e))))))))

(rf/reg-event-db
 :handle-error
 (fn [db [_ data]]
   (js/console.log "Error: " data)
   db :pokemon (:pokemon data)))

(rf/reg-event-db
 :handle-get-pokemon
 (fn [db [_ {:keys [pokemon]}]]
   (assoc db :pokemon pokemon)))

(rf/reg-event-fx
 :get-pokemon
 (fn [_ [_ pokemon-name]]
   {:fetch {:query q/pokemon
            :variables {:name pokemon-name}
            :on-success [:handle-get-pokemon]
            :on-error [:handle-error]}}))

(def ^:private default-db
  {:pokemon-name "Pikachu"
   :pokemon {}
   :pokemons []})

(rf/reg-event-fx
 :initialize
 (fn [_ _]
   {:db default-db
    :dispatch [:get-pokemon (:pokemon-name default-db)]}))