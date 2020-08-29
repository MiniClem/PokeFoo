package io.github.pokefoo.accueil

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.pokefoo.data.repository.paging.ExampleLoadStateAdapter
import io.github.pokefoo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()
{

	private lateinit var layout: ActivityMainBinding
	private val viewModel: MainActivityVM by viewModels()
	private lateinit var pokemonAdapter: PokemonAdapter

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		layout = ActivityMainBinding.inflate(layoutInflater)
		setContentView(layout.root)

		pokemonAdapter = PokemonAdapter()
		layout.mainRecycler.apply {
			layoutManager =
				GridLayoutManager(this@MainActivity, 4, GridLayoutManager.VERTICAL, false).apply {
					layoutParams
				}
			setHasFixedSize(true)
			adapter = pokemonAdapter.withLoadStateFooter(
				footer = ExampleLoadStateAdapter()
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