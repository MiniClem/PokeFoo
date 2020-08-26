package io.github.pokefoo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemon
import io.github.pokefoo.data.database.models.ownedPokemon.OwnedPokemonDao
import io.github.pokefoo.data.database.models.pokemon.PokemonEntity
import io.github.pokefoo.data.database.models.pokemon.PokemonsDao


@Database(entities = [PokemonEntity::class, OwnedPokemon::class], version = 2)
abstract class PfDatabase : RoomDatabase()
{

	abstract fun pokemonsDao(): PokemonsDao
	abstract fun ownedPokemonDao(): OwnedPokemonDao

	companion object
	{
		lateinit var db: PfDatabase

		fun init(appContext: Context)
		{
			db = Room.databaseBuilder(
                appContext,
                PfDatabase::class.java, "pokefoo-db"
            )
//                .addCallback(rdc)
				.build()
		}


//        private val rdc = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//
//
//            }
//        }
	}
}