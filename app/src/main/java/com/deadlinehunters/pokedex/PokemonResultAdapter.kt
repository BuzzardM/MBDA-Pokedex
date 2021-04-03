package com.deadlinehunters.pokedex

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.model.PokemonResult
import kotlin.math.log

class PokemonResultAdapter(private val dataSet: List<PokemonResult>, private val context: Context) : RecyclerView.Adapter<PokemonResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonResultTextView: TextView = view.findViewById(R.id.pokemon_result_item_textview)
        val pokemonResultImageView: ImageView = view.findViewById(R.id.pokemon_result_item_imageview)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_result_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentPokemonResult = dataSet[position]

        val pokemonId = String.format("%03d", (position+1))
        val uri = "@drawable/sprite_$pokemonId"
        val imageResource = context.resources.getIdentifier(
            uri,
            null,
            context.packageName
        )

        viewHolder.pokemonResultTextView.text = currentPokemonResult.name
        viewHolder.pokemonResultImageView.setImageResource(imageResource)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataSet.size
    }
}