package com.deadlinehunters.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.model.PokemonResult

class PokemonResultAdapter(private val dataSet: Array<PokemonResult>) :
    RecyclerView.Adapter<PokemonResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonResultTextView: TextView = view.findViewById(R.id.pokemon_result_item_textview)
        val pokemonResultImageView: ImageView = view.findViewById(R.id.pokemon_result_item_imageview)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pokemon_result_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentPokemonResult = dataSet[position]

        viewHolder.pokemonResultTextView.text = currentPokemonResult.name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}