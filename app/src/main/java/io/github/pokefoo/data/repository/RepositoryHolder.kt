package io.github.pokefoo.data.repository

import io.github.pokefoo.data.database.PfDatabase

object RepositoryHolder
{
	val INSTANCE by lazy {
		return@lazy DbRepository(PfDatabase.db)
	}
}