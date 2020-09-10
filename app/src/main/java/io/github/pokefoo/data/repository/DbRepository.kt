package io.github.pokefoo.data.repository

import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.repository.pokemonRepository.PokemonDataAccess
import io.github.pokefoo.data.repository.pokemonRepository.PokemonRepository

class DbRepository(
	pfDatabase: PfDatabase
)
{
	val pokemonDataAccess: PokemonDataAccess = PokemonRepository(pfDatabase)
	val pokemonOwnedRepository = PfDatabase.db.ownedPokemonDao()
}