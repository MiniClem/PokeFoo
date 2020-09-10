package io.github.pokefoo.init

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.pokefoo.accueil.MainActivity
import io.github.pokefoo.data.database.PfDatabase
import io.github.pokefoo.data.database.TaskProgressListener
import io.github.pokefoo.databinding.InitActivityBinding
import kotlinx.android.synthetic.main.init_activity.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitActivity : AppCompatActivity()
{
	private lateinit var layout: View
	private lateinit var taskProgressListener: TaskProgressListener

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		layout = InitActivityBinding.inflate(layoutInflater).root
		setContentView(layout)

		taskProgressListener = object : TaskProgressListener
		{
			override fun setMaxProgress(max: Int)
			{
				layout.progress_bar.max = max
			}

			override fun setProgress(progress: Int)
			{
				layout.progress_bar.progress = progress
			}

			override fun onFinished()
			{
				Intent(this@InitActivity, MainActivity::class.java).run {
					startActivity(this)
				}
			}
		}
	}

	override fun onResume()
	{
		super.onResume()
		lifecycleScope.launch(Dispatchers.IO) {
			PfDatabase.init(applicationContext, taskProgressListener)
			// Open the database and start the callbacks
			PfDatabase.db.pokemonsDao().getTotalCount()
		}
	}
}