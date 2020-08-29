package io.github.pokefoo.data.repository

import io.github.pokefoo.data.repository.pokemonRepository.PokemonRepository
import io.github.pokefoo.data.repository.pokemonRepository.PokemonRepositoryCache
import io.github.pokefoo.data.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class CachingRepository(
	pokeApiClient: PokeApiClient,
	pfDatabase: PfDatabase
)
{
	val pokemonRepository: PokemonRepository = PokemonRepositoryCache(pokeApiClient, pfDatabase)
	val pokemonOwnedRepository = PfDatabase.db.ownedPokemonDao()
}