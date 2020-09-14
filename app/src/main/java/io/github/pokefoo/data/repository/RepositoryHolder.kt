package io.github.pokefoo.data.repository

object RepositoryHolder
{
	val INSTANCE by lazy {
		return@lazy DbRepository()
	}
}