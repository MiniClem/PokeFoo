package io.github.pokefoo.data.repository.pokemonRepository

import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

abstract class PokemonRepository
	(
	protected val pokeApiClient: PokeApiClient,
	protected val pfDatabase: PfDatabase
)
{
	abstract suspend fun getPokemonById(id: Int): PokemonEntity?
	abstract suspend fun getOwnedPokemonList(
		offset: Int,
		count: Int
	): PokemonEntityPage<PokemonWithOwnership>

	abstract suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage<PokemonEntity>
	abstract suspend fun getPokemonCount(): Int
	abstract suspend fun getPokemonListNotOwned(): List<PokemonEntity>
}