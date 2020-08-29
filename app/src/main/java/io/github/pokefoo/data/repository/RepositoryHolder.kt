package io.github.pokefoo.data.repository

import io.github.pokefoo.data.database.PfDatabase
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

object RepositoryHolder
{
	val INSTANCE by lazy {
		return@lazy CachingRepository(PokeApiClient(), PfDatabase.db)
	}
}