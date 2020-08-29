package io.github.pokefoo.accueil

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemon.PokemonsDao
import io.github.pokefoo.data.repository.RepositoryHolder
import io.github.pokefoo.data.repository.paging.PokemonPagingResource
import io.github.pokefoo.workManager.RandomPokemonWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class MainActivityVM : ViewModel()
{

	private val pokemonsDao: PokemonsDao

	val pokemons: Flow<PagingData<PokemonEntity>> = Pager(PagingConfig(pageSize = 25)) {
		PokemonPagingResource(RepositoryHolder.INSTANCE)
	}.flow
		.cachedIn(viewModelScope)

	init
	{
		val db = PfDatabase.db
		pokemonsDao = db.pokemonsDao()
	}

	fun waitForPokemon(context: Context)
	{
		val work = OneTimeWorkRequestBuilder<RandomPokemonWorker>().apply {
			setInitialDelay(5L, TimeUnit.MINUTES)
		}.build()
		WorkManager.getInstance(context)
			.beginUniqueWork("Random Pokemon", ExistingWorkPolicy.KEEP, work)
			.enqueue()
	}
}