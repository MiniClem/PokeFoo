package io.github.pokefoo.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemonDao
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemon.PokemonTypeConverter
import io.github.pokefoo.data.database.models.pokemon.PokemonsDao
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnershipDao
import io.github.pokefoo.utils.TAG
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon


@Database(entities = [PokemonEntity::class, OwnedPokemon::class], version = 1, exportSchema = false)
abstract class PfDatabase : RoomDatabase()
{

	abstract fun pokemonsDao(): PokemonsDao
	abstract fun ownedPokemonDao(): OwnedPokemonDao
	abstract fun pokemonWithOwnershipDao(): PokemonWithOwnershipDao

	companion object
	{
		lateinit var db: PfDatabase

		fun init(appContext: Context)
		{
			db = Room.databaseBuilder(
				appContext,
				PfDatabase::class.java, "pokefoo-db"
			)
				.addCallback(rdc)
				.build()
		}

		private val rdc = object : RoomDatabase.Callback()
		{
			override fun onCreate(db: SupportSQLiteDatabase)
			{
				super.onCreate(db)
				Log.d(TAG(), "Starting ROOM callback...")

				val pokeApiClient = PokeApiClient()
				val count = pokeApiClient.getPokemonList(0, 1).count
				val listPokemons = mutableListOf<Pokemon>()
				for (i in 0 until count step 10)
				{
					pokeApiClient.getPokemonList(i, 10).results.let {
						for (namedApiResource in it)
						{
							listPokemons.add(pokeApiClient.getPokemon(namedApiResource.id))
						}
					}
					Log.d(TAG(), "Downloaded $i...")
				}

				db.beginTransaction()
				val pokemonTypeConverter = PokemonTypeConverter()
				for (p in listPokemons)
				{
					val content = ContentValues().apply {
						put("pokemon_id", p.id)
						put("name", p.name)
						put("_order", p.order)
						put("weight", p.weight)
						put("sprites", pokemonTypeConverter.fromPokemonSprites(p.sprites) ?: "")
					}
					db.insert("pokemons", SQLiteDatabase.CONFLICT_REPLACE, content)
				}
				db.setTransactionSuccessful()
				db.endTransaction()
				Log.d(TAG(), "Finishing ROOM callback...")
			}
		}
	}
}