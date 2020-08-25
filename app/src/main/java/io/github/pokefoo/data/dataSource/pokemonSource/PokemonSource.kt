package io.github.pokefoo.data.dataSource.pokemonSource

import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.PokemonEntity
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

abstract class PokemonSource
	(
	protected val pokeApiClient: PokeApiClient,
	protected val pfDatabase: PfDatabase
)
{
	abstract suspend fun getPokemonById(id: Long): PokemonEntity
	abstract suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage
}