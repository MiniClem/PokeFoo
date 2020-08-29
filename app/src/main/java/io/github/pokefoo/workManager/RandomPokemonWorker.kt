package io.github.pokefoo.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.repository.RepositoryHolder
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
		val repository = RepositoryHolder.INSTANCE
		val count = repository.pokemonRepository.getPokemonCount()
		var i = 0
		var isAlreadyOwned = true
		while (i < count && isAlreadyOwned)
		{
			val id = Random.nextInt(count)
			repository.pokemonRepository.getPokemonById(id.toLong())?.let { pokemonEntity ->
				repository.pokemonOwnedRepository.run {
					isAlreadyOwned =
						getCount(pokemonEntity.id) > 0
					if (!isAlreadyOwned)
					{
						insertAll(listOf(OwnedPokemon(pokemonId = pokemonEntity.id)))
						return Result.success()
					} else ++i
				}
			}
				?: return Result.failure()
		}
		return Result.failure() // Already owned all pokemons
	}
}