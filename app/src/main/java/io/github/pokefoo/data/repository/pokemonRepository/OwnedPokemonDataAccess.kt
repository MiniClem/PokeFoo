package io.github.pokefoo.data.repository.pokemonRepository

import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership

interface OwnedPokemonDataAccess
{
	suspend fun getOwnedPokemonList(
		offset: Int,
		count: Int
	): PokemonPage<PokemonWithOwnership>

	suspend fun addPokemons(
		pokemonEntity: List<OwnedPokemon>
	)

	suspend fun addPokemon(
		pokemonEntity: OwnedPokemon
	)
	{
		addPokemons(listOf(pokemonEntity))
	}
}