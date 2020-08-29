package io.github.pokefoo.data.database.models.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonsDao
{
	@Query("SELECT * FROM pokemons WHERE pokemons.id IN (:ids)")
	fun selectByIds(vararg ids: Long): List<PokemonEntity>

	@Query("SELECT * FROM pokemons WHERE pokemons.id IN (:ids)")
	fun selectByIds(ids: List<Long>): List<PokemonEntity>

	@Query("SELECT * FROM pokemons ORDER BY id")
	fun selectAll(): List<PokemonEntity>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertAll(vararg pokemons: PokemonEntity)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertAll(pokemons: List<PokemonEntity>)

	@Query("SELECT COUNT(*) FROM pokemons")
	fun getTotalCount(): Int
}