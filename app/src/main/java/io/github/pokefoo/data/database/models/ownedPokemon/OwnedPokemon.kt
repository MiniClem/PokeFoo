package io.github.pokefoo.data.database.models.ownedPokemon

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity

@Entity(
	tableName = "owned_pokemons",
	foreignKeys = [ForeignKey(
		entity = PokemonEntity::class,
		parentColumns = arrayOf("pokemon_id"),
		childColumns = arrayOf("_pokemon_id"),
		onDelete = ForeignKey.CASCADE
	)]
)
class OwnedPokemon(
	@PrimaryKey(autoGenerate = true)
	@NonNull
	@ColumnInfo(name = "owned_pokemon_id")
	val id: Int = 0,
	@ColumnInfo(name = "_pokemon_id")
	val pokemonId: Int
)