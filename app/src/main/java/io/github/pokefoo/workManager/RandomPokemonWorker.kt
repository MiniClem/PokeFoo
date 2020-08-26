package io.github.pokefoo.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.pokefoo.data.dataSource.CachingDataSourceHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RandomPokemonWorker(context: Context, params: WorkerParameters) :
	CoroutineWorker(context, params)
{
	override suspend fun doWork(): Result
	{
		return withContext(Dispatchers.IO) {
			val dataSource = CachingDataSourceHolder.INSTANCE
			Random.nextInt(dataSource.pokemonSource.getPokemonCount()).toLong().let { id ->
				dataSource.pokemonSource.getPokemonById(id)?.let {
					// TODO Save that pokemon for the user as owned
					return@withContext Result.success()
				}

				return@withContext Result.failure()
			}
		}
	}
}