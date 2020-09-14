package io.github.pokefoo.data.repository.pokemonRepository

import io.github.pokefoo.data.database.models.pokemon.PokemonEntity

interface PokemonDataAccess
{
	suspend fun getPokemonById(id: Int): PokemonEntity?

	suspend fun getPokemonList(offset: Int, count: Int): PokemonPage<PokemonEntity>
	suspend fun getPokemonCount(): Int
	suspend fun getPokemonListNotOwned(): List<PokemonEntity>
}