package com.deadlinehunters.pokedex.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.adapters.PokemonResultAdapter
import com.deadlinehunters.pokedex.model.Pokemon
import com.deadlinehunters.pokedex.model.PokemonResult

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    private fun getPokemonResults(requestQueue: RequestQueue) {
        val pokemonData: PokemonResult? = intent.getParcelableExtra("pokemon_data")
        val url = pokemonData?.url
        val pokemonResults = mutableListOf<Pokemon>()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val results = response.getJSONArray("results")
                
                for (i in 0 until results.length()) {
                    val result = results.getJSONObject(i)
                    pokemonResults.add(
                        Pokemon(
                            result.getString("id").toInt(),
                            result.getString("name"),
                            result.getString("height").toInt(),

                        )
                    )
                }
            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }
}