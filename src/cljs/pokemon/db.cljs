(ns pokemon.db)

(def default-db
  {:name "Pokémon"
   :pokemon-name "Pikachu"
   :pokemon {}
   :pokemons []
   :re-graph {:http-url "https://graphql-pokemon.now.sh/"
              :http-parameters {:with-credentials? false}}})
