package io.github.pokefoo

import android.app.Application
import com.facebook.stetho.Stetho

class PfApplication : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		Stetho.initializeWithDefaults(this);
	}
}