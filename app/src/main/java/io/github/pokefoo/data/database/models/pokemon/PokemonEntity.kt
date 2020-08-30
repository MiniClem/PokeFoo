package io.github.pokefoo.data.database.models.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites

@TypeConverters(PokemonTypeConverter::class)
@Entity(tableName = "pokemons")
class PokemonEntity(
	@PrimaryKey
	@ColumnInfo(name = "pokemon_id", typeAffinity = ColumnInfo.INTEGER)
	var id: Int,
	@ColumnInfo(typeAffinity = ColumnInfo.TEXT)
	var name: String,
	@ColumnInfo(name = "_order", typeAffinity = ColumnInfo.INTEGER)
	var order: Int,
	@ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
	var weight: Int,
	@ColumnInfo(typeAffinity = ColumnInfo.TEXT)
	val sprites: PokemonSprites
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
