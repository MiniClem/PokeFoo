package io.github.pokefoo

import android.app.Application
import com.facebook.stetho.Stetho
import io.github.pokefoo.data.database.PfDatabase

class PfApplication : Application()
{

	override fun onCreate()
	{
		super.onCreate()
		Stetho.initializeWithDefaults(this);
	}
}