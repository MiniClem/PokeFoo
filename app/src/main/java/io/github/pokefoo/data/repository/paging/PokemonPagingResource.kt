package io.github.pokefoo.data.repository.paging

import androidx.paging.PagingSource
import io.github.pokefoo.data.repository.CachingRepository
import io.github.pokefoo.data.repository.pokemonRepository.PokemonEntityPage
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonPagingResource(
    private val cachingRepository: CachingRepository
) : PagingSource<Int, PokemonEntity>()
{

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity>
	{
		try
		{
			// Start refresh at page 1 if undefined.
			val nextPage = params.key ?: 0
			val limit = 25
			return withContext(Dispatchers.IO) {
				val response: PokemonEntityPage =
					cachingRepository.pokemonRepository.getPokemonList(nextPage, limit)

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