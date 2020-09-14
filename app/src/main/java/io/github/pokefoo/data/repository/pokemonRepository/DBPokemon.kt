package io.github.pokefoo.data.repository.pokemonRepository

import android.util.Log
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DBPokemon : PokemonDataAccess
{
	private val pfDatabase: PfDatabase = PfDatabase.db

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

	override suspend fun getPokemonList(
		offset: Int,
		count: Int
	): PokemonPage<PokemonEntity> = withContext(Dispatchers.IO) {
		val total = async { getPokemonCount() }
		val pkFromDb = pfDatabase.pokemonsDao().selectByIds(
			listOf(offset until offset + count).flatten()
		)

		return@withContext PokemonPage(
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