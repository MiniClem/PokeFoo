package io.github.pokefoo.data.database.models.pokemonWithOwner

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PokemonWithOwnershipDao
{
	@Query(
		"""
				SELECT * 
				FROM pokemons p JOIN owned_pokemons op ON p.pokemon_id = op._pokemon_id  
				"""
	)
	fun getOwnedPokemons(): List<PokemonWithOwnership>

	@Query(
		"""
				SELECT * 
				FROM pokemons p LEFT JOIN owned_pokemons op ON p.pokemon_id = op._pokemon_id
				WHERE p.pokemon_id IN (:ids) 
				"""
	)
	fun selectByIds(ids: List<Int>): List<PokemonWithOwnership>

	@Query(
		"""
				SELECT * 
				FROM pokemons p LEFT JOIN owned_pokemons op ON p.pokemon_id = op._pokemon_id
				WHERE p.pokemon_id BETWEEN :start AND :end
				"""
	)
	fun selectByRange(start: Int, end: Int): List<PokemonWithOwnership>
}