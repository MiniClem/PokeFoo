package io.github.pokefoo.data.repository.pokemonRepository

class OwnedPokemonRepository(pokemonDataAccess: OwnedPokemonDataAccess) :
	OwnedPokemonDataAccess by pokemonDataAccess
{
	// TODO observable getters
}