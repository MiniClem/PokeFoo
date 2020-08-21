package io.github.pokefoo

import io.github.pokefoo.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

object CachingDataSourceHolder
{
	val INSTANCE by lazy {
		return@lazy PfCachingDataSource(PokeApiClient(), PfDatabase.db)
	}
}