package com.deadlinehunters.pokedex.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.activities.DetailActivity
import com.deadlinehunters.pokedex.adapters.PokemonResultAdapter
import com.deadlinehunters.pokedex.data.PokemonResult


class HomeFragment : Fragment(), PokemonResultAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = activity?.applicationContext?.let { PokemonResultAdapter(it, this)}
        val recyclerView = view?.findViewById<RecyclerView>(R.id.pokemon_overview_recyclerview)

        if (recyclerView != null) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        val requestQueue = Volley.newRequestQueue(activity?.applicationContext)
        getPokemonResults(requestQueue, adapter)
    }

    private fun getPokemonResults(requestQueue: RequestQueue, adapter: PokemonResultAdapter?) {
        val url = "https://pokeapi.co/api/v2/pokemon?limit=151"
        val pokemonResults = mutableListOf<PokemonResult>()

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val result = results.getJSONObject(i)
                    pokemonResults.add(
                        PokemonResult(
                            result.getString("name"),
                            result.getString("url")
                        )
                    )
                }
                adapter?.addPokemonResult(pokemonResults)
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }

    override fun onItemClick(pokemonResult: PokemonResult) {
        val i = Intent(requireContext(), DetailActivity::class.java)
        i.putExtra("pokemon_data", pokemonResult)

        startActivity(i)
    }
}
