package io.github.pokefoo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.pokefoo.data.database.models.PokemonEntity
import io.github.pokefoo.data.database.models.PokemonsDao


@Database(entities = [PokemonEntity::class], version = 1)
abstract class PfDatabase : RoomDatabase()
{

	abstract fun pokemonsDao(): PokemonsDao

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