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
		PfDatabase.reInit(applicationContext)
		val repository = RepositoryHolder.INSTANCE

		val count = repository.pokemonRepository.getPokemonCount()
		var i = 0
		var isAlreadyOwned = true
		while (i < count && isAlreadyOwned)
		{
			val id = Random.nextInt(count)
			Log.d(TAG(), "Trying to generate new pokemon with id : $id")
			val pokemonById = repository.pokemonRepository.getPokemonById(id)
			if (pokemonById != null)
			{
				repository.pokemonOwnedRepository.run {
					isAlreadyOwned = getCount(pokemonById.id) > 0
					if (!isAlreadyOwned)
					{
						insertAll(listOf(OwnedPokemon(pokemonId = pokemonById.id)))
						return Result.success()
					} else ++i
				}
			} else
			{
				Log.e(TAG(), "Error generating new pokemon with id : $id")
				return Result.failure()
			}
		}
		return Result.failure() // Already owned all pokemons
	}
}