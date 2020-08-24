package io.github.pokefoo

import io.github.pokefoo.database.PfDatabase
import io.github.pokefoo.database.models.PokemonEntity
import io.github.pokefoo.utils.pmap
import io.github.pokefoo.utils.toVararg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

abstract class PokemonSource
	(
	protected val pokeApiClient: PokeApiClient,
	protected val pfDatabase: PfDatabase
)
{
	abstract suspend fun getPokemonById(id: Long): PokemonEntity
	abstract suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage
}

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

	override suspend fun getPokemonById(id: Long): PokemonEntity =
		withContext(Dispatchers.IO) {
			val pkFromDb = pfDatabase.pokemonsDao().selectByIds(id)
			return@withContext if (pkFromDb.isNotEmpty()) pkFromDb.first()
			else
				pokeApiClient.getPokemon(id.toInt()).let {
					PokemonEntity.build(it).also { newPokemon ->
						pfDatabase.pokemonsDao().insertAll(newPokemon)
					}
				}
		}

	override suspend fun getPokemonList(offset: Int, count: Int): PokemonEntityPage
	{
		return withContext(Dispatchers.IO) {
			val total = pokeApiClient.getPokemonList(0, 1).count
			val pkFromDb = pfDatabase.pokemonsDao()
				.selectByIds(*(offset..(offset + count - 1).toLong()).toVararg())

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
}