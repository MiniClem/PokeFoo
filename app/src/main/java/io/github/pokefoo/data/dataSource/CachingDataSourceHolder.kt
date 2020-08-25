package io.github.pokefoo.data.dataSource

import io.github.pokefoo.data.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

object CachingDataSourceHolder
{
	val INSTANCE by lazy {
		return@lazy PfCachingDataSource(PokeApiClient(), PfDatabase.db)
	}
}