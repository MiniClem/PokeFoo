package io.github.pokefoo

import io.github.pokefoo.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PfCachingDataSource(
    pokeApiClient: PokeApiClient,
    pfDatabase: PfDatabase
) {
    val pokemonSource: PokemonSource = PokemonSourceCache(pokeApiClient, pfDatabase)

    companion object {
        val pfCachingDataSource by lazy {
            return@lazy PfCachingDataSource(PokeApiClient(), PfDatabase.db)
        }
    }
}