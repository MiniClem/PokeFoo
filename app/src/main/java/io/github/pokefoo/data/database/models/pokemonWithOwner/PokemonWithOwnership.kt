package io.github.pokefoo.data.database.models.pokemonWithOwner

import androidx.room.Embedded
import androidx.room.Entity
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity

@Entity
data class PokemonWithOwnership(
	@Embedded val pokemon: PokemonEntity,
	@Embedded val pokemonOwned: OwnedPokemon?
)

