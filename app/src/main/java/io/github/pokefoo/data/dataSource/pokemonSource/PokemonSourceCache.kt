package io.github.pokefoo.data.dataSource.pokemonSource

import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.utils.pmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

data class PokemonEntityPage(
	val pokemonEntity: List<PokemonEntity>,
	val offset: Int,
	val count: Int,
	val total: Int
)

class PokemonSourceCache(
	pokeApiClient: PokeApiClient,
	pfDatabase: PfDatabase
) : PokemonSource(pokeApiClient, pfDatabase)
{

	override suspend fun getPokemonById(id: Long): PokemonEntity? =
		withContext(Dispatchers.IO) {
			val pkFromDb = pfDatabase.pokemonsDao().selectByIds(id)
			return@withContext if (pkFromDb.isNotEmpty()) pkFromDb.first()
			else
				try
				{
					pokeApiClient.getPokemon(id.toInt()).let {
						PokemonEntity.build(it).also { newPokemon ->
							pfDatabase.pokemonsDao().insertAll(newPokemon)
						}
					}
				} catch (e: ErrorResponse)
				{
					e.printStackTrace()
					null
				}
		}

	override suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage
	{
		return withContext(Dispatchers.IO) {
			val total = getPokemonCount()
			val pkFromDb = pfDatabase.pokemonsDao()
				.selectByIds(listOf(offset.toLong() until offset + count).flatten())

			return@withContext if (pkFromDb.size == count)
				PokemonEntityPage(
					pkFromDb,
					offset, count, total
				)
			else
				pokeApiClient.getPokemonList(offset, count).run {
					PokemonEntityPage(
						results.pmap { namedApiResource ->
							pokeApiClient.getPokemon(namedApiResource.id).let { pokemon ->
								return@pmap PokemonEntity.build(pokemon)
							}
						},
						offset, count, total
					).also {
						pfDatabase.pokemonsDao().insertAll(it.pokemonEntity)
					}
				}
		}
	}

	override suspend fun getPokemonCount(): Int
	{
		return withContext(Dispatchers.IO) {
			pokeApiClient.getPokemonList(0, 1).count
		}
	}
}