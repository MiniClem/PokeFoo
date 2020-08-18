package io.github.pokefoo.accueil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.pokefoo.databinding.PokemonItemBinding
import kotlinx.android.synthetic.main.pokemon_item.view.*
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class PokemonAdapter : PagingDataAdapter<Pokemon, PokemonAdapter.PokemonAdapterVH>(
    PokemonApiResourceComparator
) {

    inner class PokemonAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    private object PokemonApiResourceComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapterVH {
        val view = PokemonItemBinding.inflate(LayoutInflater.from(parent.context))
        return PokemonAdapterVH(view.root)
    }

    override fun onBindViewHolder(holder: PokemonAdapterVH, position: Int) {
        getItem(position)?.let { pokemon ->
            holder.itemView.pokemon_item_name.apply {
                text = pokemon.name
            }

            holder.itemView.pokemon_item_icon.apply {
                Glide.with(this).load(pokemon.sprites.frontDefault).centerCrop().into(this)
            }
        }
    }
}