package io.github.pokefoo.accueil

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import io.github.pokefoo.R
import io.github.pokefoo.data.repository.paging.ExampleLoadStateAdapter
import io.github.pokefoo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()
{

	private lateinit var layout: ActivityMainBinding
	private val viewModel: MainActivityVM by viewModels()
	private lateinit var pokemonAdapter: PokemonAdapter

	@SuppressLint("UseCompatLoadingForDrawables")
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		layout = ActivityMainBinding.inflate(layoutInflater)
		setContentView(layout.root)

		pokemonAdapter = PokemonAdapter(this)
		layout.mainRecycler.apply {
			layoutManager = GridLayoutManager(
				this@MainActivity,
				4,
				GridLayoutManager.VERTICAL,
				false
			)
			setHasFixedSize(true)
			adapter = pokemonAdapter.withLoadStateFooter(
				footer = ExampleLoadStateAdapter()
			)
			addItemDecoration(
				DividerItemDecoration(
					this@MainActivity,
					(layoutManager as GridLayoutManager).orientation
				).apply {
					setDrawable(
						this@MainActivity.resources.getDrawable(
							R.drawable.dr_basic_divider,
							null
						)
					)
				}
			)
		}

		layout.floatingActionButton.setOnClickListener { viewModel.waitForPokemon(this) }
	}

	override fun onStart()
	{
		super.onStart()
		lifecycleScope.launch {
			viewModel.pokemons.collectLatest {
				pokemonAdapter.submitData(it)
			}
		}
	}
}