(ns pokemon.queries)

(def pokemon
  "
query ($name: String!){
  pokemon(name: $name) {
    id
    number
    image
    name
    types
  }
}
")

(defn pokemons
  ([] (pokemons 10))
  ([first]
   (str "
  pokemons(first: " first ") {
    id
    name
  }
")))
