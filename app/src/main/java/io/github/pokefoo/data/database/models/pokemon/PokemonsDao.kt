package io.github.pokefoo.data.database.models.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonsDao
{
	@Query("SELECT * FROM pokemons WHERE pokemons.pokemon_id IN (:ids)")
	fun selectByIds(vararg ids: Int): List<PokemonEntity>

	@Query("SELECT * FROM pokemons WHERE pokemons.pokemon_id IN (:ids)")
	fun selectByIds(ids: List<Int>): List<PokemonEntity>

	@Query("SELECT * FROM pokemons ORDER BY pokemon_id")
	fun selectAll(): List<PokemonEntity>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertAll(vararg pokemons: PokemonEntity)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertAll(pokemons: List<PokemonEntity>)

	@Query("SELECT COUNT(*) FROM pokemons")
	fun getTotalCount(): Int
}