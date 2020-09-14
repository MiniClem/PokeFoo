package io.github.pokefoo.data.repository.pokemonRepository

import android.util.Log
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import io.github.pokefoo.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DBOwnedPokemon : OwnedPokemonDataAccess
{
	private val pfDatabase = PfDatabase.db

	override suspend fun getOwnedPokemonList(
		offset: Int,
		count: Int
	): PokemonPage<PokemonWithOwnership> = withContext(Dispatchers.IO) {
		val total = async { pfDatabase.pokemonsDao().getTotalCount() }
		val end = offset + count
		Log.d(TAG(), "Ids requested for owned pokemons list : from $offset to $end")
		val pkFromDb = pfDatabase.pokemonWithOwnershipDao().selectByRange(offset, end)

		return@withContext PokemonPage(
			pkFromDb,
			offset, count, total.await()
		)
	}

	override suspend fun addPokemons(pokemonEntity: List<OwnedPokemon>) =
		withContext(Dispatchers.IO)
		{
			pfDatabase.ownedPokemonDao().insertAll(pokemonEntity)
		}
}