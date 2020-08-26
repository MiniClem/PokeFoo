package io.github.pokefoo.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.pokefoo.data.dataSource.CachingDataSourceHolder
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
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
		val dataSource = CachingDataSourceHolder.INSTANCE
		val count = dataSource.pokemonSource.getPokemonCount()
		var i = 0
		var isAlreadyOwned = true
		while (i < count && isAlreadyOwned)
			Random.nextInt(count).toLong().let { id ->
				dataSource.pokemonSource.getPokemonById(id)?.let { pokemonEntity ->
					isAlreadyOwned =
						PfDatabase.db.ownedPokemonDao().getCount(pokemonEntity.id) > 0
					if (!isAlreadyOwned)
					{
						PfDatabase.db.ownedPokemonDao()
							.insertAll(listOf(OwnedPokemon(pokemonId = pokemonEntity.id)))
						return Result.success()
					} else ++i
				}
					?: return Result.failure()
			}
		return Result.failure() // Already owned all pokemons
	}
}