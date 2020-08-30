package io.github.pokefoo.data.repository.pokemonRepository

import android.util.Log
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import io.github.pokefoo.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PokemonRepositoryCache(
	pokeApiClient: PokeApiClient,
	pfDatabase: PfDatabase
) : PokemonRepository(pokeApiClient, pfDatabase)
{

	override suspend fun getPokemonById(id: Int): PokemonEntity? = withContext(Dispatchers.IO) {
		val pkFromDb = pfDatabase.pokemonsDao().selectByIds(id)
		return@withContext if (pkFromDb.isNotEmpty())
			pkFromDb.first()
		else
		{
			Log.e(TAG(), "Impossible to get pokemon by id : $id")
			null
		}
	}

	override suspend fun getOwnedPokemonList(
		offset: Int,
		count: Int
	): PokemonEntityPage<PokemonWithOwnership> = withContext(Dispatchers.IO) {
		val total = async { getPokemonCount() }
		val ids = listOf(offset until offset + count).flatten()
		Log.d(TAG(), "Ids requested for owned pokemons list : $ids")
		val pkFromDb = pfDatabase.pokemonWithOwnershipDao().selectByIds(
			ids
		)

		return@withContext PokemonEntityPage(
			pkFromDb,
			offset, count, total.await()
		)
	}


	override suspend fun getPokemonList(
		offset: Int,
		count: Int
	): PokemonEntityPage<PokemonEntity> = withContext(Dispatchers.IO) {
		val total = async { getPokemonCount() }
		val pkFromDb = pfDatabase.pokemonsDao().selectByIds(
			listOf(offset until offset + count).flatten()
		)

		return@withContext PokemonEntityPage(
			pkFromDb,
			offset, count, total.await()
		)
	}

	override suspend fun getPokemonCount(): Int = withContext(Dispatchers.IO) {
		pfDatabase.pokemonsDao().getTotalCount()
	}
}