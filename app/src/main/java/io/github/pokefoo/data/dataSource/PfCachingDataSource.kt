package io.github.pokefoo.data.dataSource

import io.github.pokefoo.data.dataSource.pokemonSource.PokemonSource
import io.github.pokefoo.data.dataSource.pokemonSource.PokemonSourceCache
import io.github.pokefoo.data.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PfCachingDataSource(
	pokeApiClient: PokeApiClient,
	pfDatabase: PfDatabase
)
{
	val pokemonSource: PokemonSource = PokemonSourceCache(pokeApiClient, pfDatabase)
}