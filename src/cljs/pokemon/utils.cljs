(ns pokemon.utils)

(def not-nil? (complement nil?))

(defn get-target-value
  [event]
  (-> event
      .-target
      .-value))     