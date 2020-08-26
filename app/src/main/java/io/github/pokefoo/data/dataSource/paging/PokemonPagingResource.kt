package io.github.pokefoo.data.dataSource.paging

import androidx.paging.PagingSource
import io.github.pokefoo.data.dataSource.PfCachingDataSource
import io.github.pokefoo.data.dataSource.pokemonSource.PokemonEntityPage
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonPagingResource(
    private val cachingDataSource: PfCachingDataSource
) : PagingSource<Int, PokemonEntity>()
{

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity>
	{
		try
		{
			// Start refresh at page 1 if undefined.
			val nextPage = params.key ?: 0
			val limit = 24
			return withContext(Dispatchers.IO) {
				val response: PokemonEntityPage =
					cachingDataSource.pokemonSource.getPokemonList(nextPage, limit)

				// Make sure that previous page can't go before 0
				val prevKey = when
				{
					nextPage == 0 -> null
					(nextPage - limit) > 0 -> (nextPage - limit)
					else -> 0
				}

				// Make sure that previous page can't go after result count
				val nextKey = when
				{
					nextPage + limit <= response.total -> nextPage + limit
					else -> null
				}

				return@withContext LoadResult.Page(
                    data = response.pokemonEntity,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
			}
		} catch (e: Exception)
		{
			e.printStackTrace()
			return LoadResult.Error(e)
		}
	}
}