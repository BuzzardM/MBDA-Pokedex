package com.deadlinehunters.pokedex.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.activities.DetailActivity
import com.deadlinehunters.pokedex.adapters.PokemonFavoriteAdapter
import com.deadlinehunters.pokedex.data.Pokemon
import com.deadlinehunters.pokedex.data.PokemonViewModel

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

        val adapter =
            activity?.applicationContext?.let { PokemonFavoriteAdapter(it, this) }
        val recyclerView = view?.findViewById<RecyclerView>(R.id.pokemon_favorites_recyclerview)

        if (recyclerView != null) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(activity?.applicationContext, 5)
        }

        val mPokemonViewModel: PokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        mPokemonViewModel.getAllPokemon().observe(viewLifecycleOwner, Observer { pokemon ->
            adapter?.addPokemon(pokemon)
        })
    }

    override fun onItemClick(pokemon: Pokemon) {
        val i = Intent(requireContext(), DetailActivity::class.java)
        i.putExtra("favorite_pokemon", pokemon)
        startActivity(i)
    }
}