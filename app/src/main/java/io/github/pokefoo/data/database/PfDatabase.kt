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

		fun init(appContext: Context, taskProgressListener: TaskProgressListener? = null)
		{
			db = Room.databaseBuilder(
				appContext,
				PfDatabase::class.java, "pokefoo-db"
			).run {
				taskProgressListener?.let {
					addCallback(Callback(taskProgressListener))
				}
				build()
			}
		}

		/**
		 * Callback to create the initial pokemon datas in development !!
		 * Production-ready app is given an already packed db in /assets folder
		 */
		class Callback(
			private val taskProgressListener: TaskProgressListener
		) : RoomDatabase.Callback()
		{
			override fun onOpen(db: SupportSQLiteDatabase)
			{
				super.onOpen(db)
				Log.d(TAG(), "Database opened...")
				taskProgressListener.onFinished()
			}

			override fun onCreate(db: SupportSQLiteDatabase)
			{
				super.onCreate(db)
				Log.d(TAG(), "Starting ROOM callback...")
				val pokeApiClient = PokeApiClient()
				val count = pokeApiClient.getPokemonList(0, 1).count
				taskProgressListener.setMaxProgress(count)
				val listPokemons = mutableListOf<Pokemon>()
				for (i in 0 until count step 50)
				{
					pokeApiClient.getPokemonList(i, 50).results.let {
						for (namedApiResource in it)
						{
							listPokemons.add(pokeApiClient.getPokemon(namedApiResource.id))
						}
					}
					taskProgressListener.setProgress(i)
					Log.d(TAG(), "Downloaded $i...")
				}

				db.beginTransaction()
				val pokemonTypeConverter = PokemonTypeConverter()
				taskProgressListener.setMaxProgress(listPokemons.size)

				for ((index, p) in listPokemons.withIndex())
				{
					val content = ContentValues().apply {
						put("pokemon_id", p.id)
						put("name", p.name)
						put("_order", p.order)
						put("weight", p.weight)
						put("sprites", pokemonTypeConverter.fromPokemonSprites(p.sprites) ?: "")
					}
					db.insert("pokemons", SQLiteDatabase.CONFLICT_REPLACE, content)
					taskProgressListener.setProgress(index)
				}
				db.setTransactionSuccessful()
				db.endTransaction()
				Log.d(TAG(), "Finishing ROOM callback...")
			}
		}
	}
}