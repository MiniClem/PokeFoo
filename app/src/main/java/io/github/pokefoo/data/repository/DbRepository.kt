package io.github.pokefoo.data.repository

import io.github.pokefoo.data.repository.pokemonRepository.*

class DbRepository
{
	val pokemonRepository: PokemonDataAccess = PokemonRepository(DBPokemon())
	val pokemonOwnedRepository: OwnedPokemonDataAccess = OwnedPokemonRepository(DBOwnedPokemon())
}