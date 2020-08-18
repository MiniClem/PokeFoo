package io.github.pokefoo

import io.github.pokefoo.database.PfDatabase
import io.github.pokefoo.database.models.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

interface PokemonSource {
    val pokeApiClient: PokeApiClient
    val pfDatabase: PfDatabase

    suspend fun getPokemonById(id: Long): PokemonEntity
    suspend fun getPokemonList(offset: Int, limit: Int): List<PokemonEntity>
}

class PokemonSourceCache(
    override val pokeApiClient: PokeApiClient,
    override val pfDatabase: PfDatabase
) : PokemonSource {

    override suspend fun getPokemonById(id: Long): PokemonEntity {
        val pkFromDb = pfDatabase.pokemonsDao().selectByIds(id)
        return if (pkFromDb.isNotEmpty()) pkFromDb.first()
        else
            pokeApiClient.getPokemon(id.toInt()).let {
                PokemonEntity(
                    id = it.id,
                    name = it.name,
                    order = it.order,
                    sprites = it.sprites,
                    weight = it.weight
                ).also { newPokemon ->
                    withContext(Dispatchers.IO) {
                        pfDatabase.pokemonsDao().insertAll(newPokemon)
                    }
                }
            }
    }

    override suspend fun getPokemonList(offset: Int, limit: Int): List<PokemonEntity> {
        TODO("Not yet implemented")
    }
}