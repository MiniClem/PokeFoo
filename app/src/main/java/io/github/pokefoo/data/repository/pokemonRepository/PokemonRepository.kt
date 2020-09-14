package io.github.pokefoo.data.repository.pokemonRepository

/**
 * Intermediate layer to access pokemons
 */
class PokemonRepository(pokemonDataAccess: PokemonDataAccess) :
	PokemonDataAccess by pokemonDataAccess
{
	///TODO observable data getter
}