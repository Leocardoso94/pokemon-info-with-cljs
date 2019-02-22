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

(def pokemons
  "
query ($first: Int!){
  pokemons(first: $first) {
    id
    name
  }
} 
")
