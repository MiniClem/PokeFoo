package io.github.pokefoo.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PokemonsDao {
    @Query("SELECT * FROM pokemons WHERE pokemons.id IN (:ids)")
    fun selectByIds(vararg ids: Long): List<PokemonEntity>

    @Query("SELECT * FROM pokemons ORDER BY id")
    fun selectAll(): List<PokemonEntity>

    @Insert
    fun insertAll(vararg pokemons: PokemonEntity)
}