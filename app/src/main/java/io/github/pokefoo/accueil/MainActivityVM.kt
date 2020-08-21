package io.github.pokefoo.accueil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.pokefoo.CachingDataSourceHolder
import io.github.pokefoo.PokemonPagingResource
import io.github.pokefoo.database.PfDatabase
import io.github.pokefoo.database.models.PokemonEntity
import io.github.pokefoo.database.models.PokemonsDao
import kotlinx.coroutines.flow.Flow

class MainActivityVM : ViewModel()
{

	private val pokemonsDao: PokemonsDao

	val pokemons: Flow<PagingData<PokemonEntity>> = Pager(PagingConfig(pageSize = 24)) {
		PokemonPagingResource(CachingDataSourceHolder.INSTANCE)
	}.flow
		.cachedIn(viewModelScope)

	init
	{
		val db = PfDatabase.db
		pokemonsDao = db.pokemonsDao()
	}
}