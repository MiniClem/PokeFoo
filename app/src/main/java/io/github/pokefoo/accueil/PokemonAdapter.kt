package io.github.pokefoo.accueil

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.pokefoo.R
import io.github.pokefoo.data.database.models.pokemonWithOwner.PokemonWithOwnership
import io.github.pokefoo.databinding.PokemonItemBinding
import kotlinx.android.synthetic.main.pokemon_item.view.*
import kotlin.math.roundToInt

class PokemonAdapter(context: Context) :
	PagingDataAdapter<PokemonWithOwnership, PokemonAdapter.PokemonAdapterVH>(
		PokemonApiResourceComparator
	)
{

	private val glide = Glide.with(context)

	private val pxSizeEmpty: Int = TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		40F,
		context.resources.displayMetrics
	).roundToInt()

	private val pxSizePokemonIcon: Int = TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		100F,
		context.resources.displayMetrics
	).roundToInt()

	class PokemonAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView)

	private object PokemonApiResourceComparator : DiffUtil.ItemCallback<PokemonWithOwnership>()
	{
		override fun areItemsTheSame(
			oldItem: PokemonWithOwnership,
			newItem: PokemonWithOwnership
		): Boolean
		{
			// Id is unique.
			return oldItem.pokemon.id == newItem.pokemon.id
		}

		override fun areContentsTheSame(
			oldItem: PokemonWithOwnership,
			newItem: PokemonWithOwnership
		): Boolean
		{
			return oldItem == newItem
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapterVH
	{
		return PokemonAdapterVH(PokemonItemBinding.inflate(LayoutInflater.from(parent.context)).root)
	}

	override fun onBindViewHolder(holder: PokemonAdapterVH, position: Int)
	{
		getItem(position)?.let { pokemon ->
			with(holder.itemView) {
				pokemon_item_name.text = pokemon.pokemon.name

				with(pokemon_item_icon) {
					val isPokemonOwned = pokemon.pokemonOwned != null
					val image =
						if (isPokemonOwned) pokemon.pokemon.sprites.frontDefault else R.drawable.ic_pokemon
					val pxSize = if (isPokemonOwned) pxSizePokemonIcon else pxSizeEmpty

					glide.load(image)
						.apply(RequestOptions().override(pxSize, pxSize))
						.fitCenter()
						.into(this)
				}
			}
		}
	}
}