package io.github.pokefoo.data.database.models.ownedPokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owned_pokemons")
class OwnedPokemon(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	@ColumnInfo(name = "pokemon_id")
	val pokemonId: Int
)