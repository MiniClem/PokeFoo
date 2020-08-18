package io.github.pokefoo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites

@TypeConverters(PokemonTypeConverter::class)
@Entity(tableName = "pokemons")
class PokemonEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var order: Int,
    var weight: Int,
    val sprites: PokemonSprites
)

//    @ColumnInfo(name = "base_experience")
//    val baseExperience: Int = pokemon.baseExperience
//    val height: Int = pokemon.height

//    @ColumnInfo(name = "is_default")
//    val isDefault: Boolean = pokemon.isDefault

//    val species: NamedApiResource = pokemon.species
//    val abilities: List<PokemonAbility> = pokemon.abilities
//    val forms: List<NamedApiResource> = pokemon.forms

//    @ColumnInfo(name = "game_indices")
//    val gameIndices: List<VersionGameIndex> = pokemon.gameIndices

//    @ColumnInfo(name = "held_items")
//    val heldItems: List<PokemonHeldItem> = pokemon.heldItems
//    val moves: List<PokemonMove> = pokemon.moves
//    val stats: List<PokemonStat> = pokemon.stats
//    val types: List<PokemonType> = pokemon.types
