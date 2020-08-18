package io.github.pokefoo

import androidx.paging.PagingSource
import io.github.pokefoo.database.models.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonPagingResource(
    private val pfCachingDataSource: PfCachingDataSource
) : PagingSource<Int, PokemonEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0
            val limit = 12
            return withContext(Dispatchers.IO) {
                val response = pfCachingDataSource.pokemonSource.getPokemonList(nextPage, limit)

                // Make sure that previous page can't go before 0
                val prevKey = when {
                    (nextPage - limit) > 0 -> (nextPage - limit)
                    else -> null
                }

                // Make sure that previous page can't go after result count
                val nextKey = when {
                    response.isNotEmpty() -> nextPage + limit
                    else -> null
                }

                return@withContext LoadResult.Page(
                    data = response,
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