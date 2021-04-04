package com.deadlinehunters.pokedex.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.model.Pokemon
import com.deadlinehunters.pokedex.model.PokemonResult
import com.deadlinehunters.pokedex.model.PokemonStat
import com.deadlinehunters.pokedex.model.PokemonType

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.hide()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        getPokemonResults(requestQueue)
        return super.onCreateView(name, context, attrs)
    }

    private fun getPokemonResults(requestQueue: RequestQueue) {
        val pokemonData: PokemonResult? = intent.getParcelableExtra("pokemon_data")
        val url = pokemonData?.url
        val pokemon = mutableListOf<Pokemon>()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val height = response.getInt("height")
                val weight = response.getInt("weight")
                val id = response.getInt("id")
                val name = response.getString("name")
                val jsonStats = response.getJSONArray("stats")
                val jsonTypes = response.getJSONArray("types")

                val stats = mutableListOf<PokemonStat>()
                val types = mutableListOf<PokemonType>()

                for (i in 0 until jsonStats.length()) {
                    val result = jsonStats.getJSONObject(i)
                    stats.add(
                        PokemonStat(
                            result.getInt("base_stat"),
                            result.getJSONObject("stat").getString("name")
                        )
                    )
                }

                for (i in 0 until jsonTypes.length()) {
                    val result = jsonTypes.getJSONObject(i)
                    types.add(
                        PokemonType(
                            result.getInt("slot"),
                            result.getJSONObject("type").getString("name")
                        )
                    )
                }

                pokemon.add(Pokemon(id, name, height, weight, stats, types))


            },
            { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(request)
    }
}