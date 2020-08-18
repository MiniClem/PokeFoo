package io.github.pokefoo

import androidx.paging.PagingSource
import io.github.pokefoo.utils.pmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class PokemonPagingResource(
    private val pokeApiClient: PokeApiClient
) : PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0
            val limit = 12
            return withContext(Dispatchers.IO) {
                val response = pokeApiClient.getPokemonList(nextPage, limit)

                // Make sure that previous page can't go before 0
                val prevKey = when {
                    (nextPage - limit) > 0 -> (nextPage - limit)
                    else -> null
                }

                // Make sure that previous page can't go after result count
                val nextKey = when {
                    nextPage + limit <= response.count -> nextPage + limit
                    else -> null
                }

                val map = response.results.pmap {
                    pokeApiClient.getPokemon(it.id)
                }

                return@withContext LoadResult.Page(
                    data = map,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }
}