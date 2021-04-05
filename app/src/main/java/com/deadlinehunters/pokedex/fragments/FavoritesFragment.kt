package com.deadlinehunters.pokedex.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.adapters.PokemonFavoriteAdapter
import com.deadlinehunters.pokedex.adapters.PokemonResultAdapter
import com.deadlinehunters.pokedex.data.Pokemon

class FavoritesFragment : Fragment(), PokemonFavoriteAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pokemon = mutableListOf<Pokemon>()

        // TODO: GET POKEMON FROM DATABASE

        val adapter =
            activity?.applicationContext?.let { PokemonFavoriteAdapter(pokemon, it, this) }
        val recyclerView = view?.findViewById<RecyclerView>(R.id.pokemon_favorites_recyclerview)

        if (recyclerView != null) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(activity?.applicationContext, 5)
        }

    }

    override fun onItemClick(pokemon: Pokemon) {
        TODO("Not yet implemented")
    }
}