package io.github.pokefoo.data.repository.pokemonRepository

import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

abstract class PokemonRepository
	(
	protected val pokeApiClient: PokeApiClient,
	protected val pfDatabase: PfDatabase
)
{
	abstract suspend fun getPokemonById(id: Long): PokemonEntity?
	abstract suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage
	abstract suspend fun getPokemonCount(): Int
}