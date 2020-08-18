package io.github.pokefoo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.pokefoo.database.models.PokemonEntity
import io.github.pokefoo.database.models.PokemonsDao
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList


@Database(entities = [PokemonEntity::class], version = 1)
abstract class PfDatabase : RoomDatabase() {

    abstract fun pokemonsDao(): PokemonsDao

    companion object {
        lateinit var db: PfDatabase

        fun init(appContext: Context) {
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