package io.github.pokefoo.data.repository.pokemonRepository

data class PokemonPage<T>(
	val pokemonEntity: List<T>,
	val offset: Int,
	val count: Int,
	val total: Int
)