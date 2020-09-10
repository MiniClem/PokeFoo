package io.github.pokefoo.data.repository.pokemonRepository

import android.util.Log
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import io.github.pokefoo.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Intermediate layer to access pokemon data
 */
class PokemonRepository(
	private val pfDatabase: PfDatabase
) : PokemonDataAccess
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
		val end = offset + count
		Log.d(TAG(), "Ids requested for owned pokemons list : from $offset to $end")
		val pkFromDb = pfDatabase.pokemonWithOwnershipDao().selectByRange(offset, end)

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

	override suspend fun getPokemonListNotOwned(): List<PokemonEntity> =
		withContext(Dispatchers.IO) {
			return@withContext pfDatabase.pokemonsDao().selectAllNotOwned()
		}
}