package io.github.pokefoo.accueil

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import io.github.pokefoo.data.repository.RepositoryHolder
import io.github.pokefoo.data.repository.paging.PokemonPagingResource
import io.github.pokefoo.workManager.RandomPokemonWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class MainActivityVM : ViewModel()
{
	val pokemons: Flow<PagingData<PokemonWithOwnership>> = Pager(PagingConfig(pageSize = 25)) {
		PokemonPagingResource(RepositoryHolder.INSTANCE)
	}.flow
		.cachedIn(viewModelScope)

	fun waitForPokemon(context: Context)
	{
		val work = OneTimeWorkRequestBuilder<RandomPokemonWorker>().apply {
			setInitialDelay(5L, TimeUnit.MINUTES)
		}
			.setConstraints(Constraints.Builder()
				.setRequiresBatteryNotLow(true)
				.build())
			.build()
		WorkManager.getInstance(context)
			.beginUniqueWork("Random Pokemon", ExistingWorkPolicy.KEEP, work)
			.enqueue()
	}
}