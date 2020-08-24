package io.github.pokefoo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites

@TypeConverters(PokemonTypeConverter::class)
@Entity(tableName = "pokemons")
class PokemonEntity(
	@PrimaryKey
	var id: Int,
	var name: String,
	var order: Int,
	var weight: Int,
	val sprites: PokemonSprites,
	val obtained: Boolean = false
)
{
	companion object Factory
	{
		fun build(pokemon: Pokemon) = PokemonEntity(
			id = pokemon.id,
			name = pokemon.name,
			order = pokemon.order,
			weight = pokemon.weight,
			sprites = pokemon.sprites
		)
	}

	override fun equals(other: Any?): Boolean
	{
		return other is PokemonEntity && other.hashCode() == this.hashCode()
	}

	override fun hashCode(): Int
	{
		var result = id
		result = 31 * result + name.hashCode()
		result = 31 * result + order
		result = 31 * result + weight
		result = 31 * result + sprites.hashCode()
		return result
	}
}
