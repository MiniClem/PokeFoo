package io.github.pokefoo.data.database.models.ownedPokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OwnedPokemonDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertAll(list: List<OwnedPokemon>)

	@Query("SELECT COUNT(*) FROM owned_pokemons WHERE pokemon_id = :pokemonId")
	fun getCount(pokemonId: Int): Int
}