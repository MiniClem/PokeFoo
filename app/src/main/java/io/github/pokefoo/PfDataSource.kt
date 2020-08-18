package io.github.pokefoo

import io.github.pokefoo.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PfDataSource (
    pokeApiClient: PokeApiClient,
    pfDatabase: PfDatabase
) {

    val pokemonSource: PokemonSource

    init {
        pokemonSource = PokemonSourceCache(pokeApiClient, pfDatabase)
    }

}