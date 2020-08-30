package io.github.pokefoo.data.repository.pokemonRepository

data class PokemonEntityPage<T>(
	val pokemonEntity: List<T>,
	val offset: Int,
	val count: Int,
	val total: Int
)