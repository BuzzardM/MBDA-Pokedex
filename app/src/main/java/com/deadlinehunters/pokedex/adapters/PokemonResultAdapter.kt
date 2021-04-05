package com.deadlinehunters.pokedex.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.data.PokemonResult

class PokemonResultAdapter(
    private val dataSet: List<PokemonResult>,
    private val context: Context,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<PokemonResultAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val pokemonResultNameTextView: TextView = view.findViewById(R.id.pokemon_result_item_name_textview)
        val pokemonResultIdTextView: TextView = view.findViewById(R.id.pokemon_result_item_id_textview)
        val pokemonResultImageView: ImageView = view.findViewById(R.id.pokemon_result_item_imageview)

        init{
           view.setOnClickListener(this)

            val preferences = PreferenceManager.getDefaultSharedPreferences(view.context)

            val font: Int = when (preferences.getString("pokemon_font_preference", "normal")) {
                "normal" -> R.font.pokemon_font
                "unown" -> R.font.pokemon_unown_font
                else -> {
                    R.font.pokemon_font
                }
            }

            pokemonResultNameTextView.typeface = ResourcesCompat.getFont(view.context, font)
        }
        override fun onClick(v: View?) {
            val position: Int = adapterPosition

            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(dataSet[position])
        }
    }

    interface OnItemClickListener{
        fun onItemClick(pokemonResult: PokemonResult)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_result_item, viewGroup, false)
        
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
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

        viewHolder.pokemonResultNameTextView.text = currentPokemonResult.name
        viewHolder.pokemonResultIdTextView.text = "No. $pokemonId"
        viewHolder.pokemonResultImageView.setImageResource(imageResource)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataSet.size
    }


}
