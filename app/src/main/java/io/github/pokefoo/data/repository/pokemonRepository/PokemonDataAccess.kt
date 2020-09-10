package io.github.pokefoo.data.repository.pokemonRepository

import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership

interface PokemonDataAccess
{
	suspend fun getPokemonById(id: Int): PokemonEntity?
	suspend fun getOwnedPokemonList(
		offset: Int,
		count: Int
	): PokemonEntityPage<PokemonWithOwnership>

	suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage<PokemonEntity>
	suspend fun getPokemonCount(): Int
	suspend fun getPokemonListNotOwned(): List<PokemonEntity>
}