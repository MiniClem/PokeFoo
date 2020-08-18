package io.github.pokefoo.accueil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.pokefoo.PokemonPagingResource
import io.github.pokefoo.database.PfDatabase
import io.github.pokefoo.database.models.PokemonsDao
import kotlinx.coroutines.flow.Flow
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class MainActivityVM : ViewModel() {

    private val pokemonsDao: PokemonsDao

    val pokemons: Flow<PagingData<Pokemon>> = Pager(PagingConfig(pageSize = 12)) {
        PokemonPagingResource(PokeApiClient())
    }.flow
        .cachedIn(viewModelScope)

    init {
        val db = PfDatabase.db
        pokemonsDao = db.pokemonsDao()
    }
}