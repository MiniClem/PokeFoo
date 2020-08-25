package io.github.pokefoo

import android.app.Application
import io.github.pokefoo.data.database.PfDatabase

class PfApplication : Application()
{

	override fun onCreate()
	{
		super.onCreate()
		PfDatabase.init(applicationContext)
	}
}