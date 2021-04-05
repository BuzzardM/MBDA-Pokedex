package com.deadlinehunters.pokedex.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.data.Pokemon

class PokemonFavoriteAdapter(
    private val dataSet: List<Pokemon>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PokemonFavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val pokemonFavoriteNameTextView: TextView =
            view.findViewById(R.id.pokemon_favorite_item_textview)
        val pokemonFavoriteImageView: ImageView =
            view.findViewById(R.id.pokemon_favorite_item_imageview)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition

            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(dataSet[position])
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(pokemon: Pokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_favorite_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPokemonResult = dataSet[position]

        val pokemonId = String.format("%03d", (currentPokemonResult.id))
        val uri = "@drawable/sprite_$pokemonId"
        val imageResource = context.resources.getIdentifier(
            uri,
            null,
            context.packageName
        )

        viewHolder.pokemonFavoriteNameTextView.text = currentPokemonResult.name
        viewHolder.pokemonFavoriteImageView.setImageResource(imageResource)
    }

    override fun getItemCount() = dataSet.size

}