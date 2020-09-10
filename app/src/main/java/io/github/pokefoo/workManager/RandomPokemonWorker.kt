package io.github.pokefoo.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.repository.RepositoryHolder
import io.github.pokefoo.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RandomPokemonWorker(context: Context, params: WorkerParameters) :
	CoroutineWorker(context, params)
{
	override suspend fun doWork(): Result
	{
		return withContext(Dispatchers.IO) {
			generateNewPokemon()
		}
	}

	private suspend fun generateNewPokemon(): Result
	{
		PfDatabase.init(applicationContext)
		val repository = RepositoryHolder.INSTANCE
		val pokemonNotOwned = repository.pokemonDataAccess.getPokemonListNotOwned()
		if (pokemonNotOwned.isEmpty())
		{
			Log.d(TAG(), "No pokemon not owned..")
			return Result.failure() // Already owned all pokemons
		}
		val r = Random.Default
		val index = r.nextInt(pokemonNotOwned.size)
		Log.d(TAG(), "Choosing pokemon at index : $index")
		Log.d(TAG(), "New pokemon found with id : ${pokemonNotOwned[index].id}")
		repository.pokemonOwnedRepository.insertAll(listOf(OwnedPokemon(pokemonId = pokemonNotOwned[index].id)))

		return Result.success()
	}
}