(ns pokemon.db)

(def default-db
  {:name "Pokemon information"
   :pokemon-name "Pikachu"
   :pokemon nil
   :re-graph {:http-url "https://graphql-pokemon.now.sh/"
              :http-parameters {:with-credentials? false}}})
