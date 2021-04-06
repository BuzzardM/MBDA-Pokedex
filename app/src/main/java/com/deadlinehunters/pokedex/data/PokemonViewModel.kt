package com.deadlinehunters.pokedex.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.deadlinehunters.pokedex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PokemonRepository

    init {
        val pokemonDao = PokemonDatabase.getDatabase(application).pokemonDAO()
        repository = PokemonRepository(pokemonDao)
    }

    fun addPokemon(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPokemon(pokemon)
        }
    }

    fun getAllPokemon(): LiveData<List<Pokemon>> = repository.getAllPokemon()

    fun updatePokemon(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePokemon(pokemon)
        }
    }

    fun deletePokemon(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePokemon(pokemonId)
        }
    }

    fun getColorStringFromType(typeName: String, context: Context) : String {
        val colorString: String
        when (typeName) {
            "normal" -> colorString = context.getString(R.string.pokemon_type_normal_hexstring)
            "fire" -> colorString = context.getString(R.string.pokemon_type_fire_hexstring)
            "water" -> colorString = context.getString(R.string.pokemon_type_water_hexstring)
            "electric" -> colorString = context.getString(R.string.pokemon_type_electric_hexstring)
            "grass" -> colorString = context.getString(R.string.pokemon_type_grass_hexstring)
            "ice" -> colorString = context.getString(R.string.pokemon_type_ice_hexstring)
            "fighting" -> colorString = context.getString(R.string.pokemon_type_fighting_hexstring)
            "poison" -> colorString = context.getString(R.string.pokemon_type_poison_hexstring)
            "ground" -> colorString = context.getString(R.string.pokemon_type_ground_hexstring)
            "flying" -> colorString = context.getString(R.string.pokemon_type_flying_hexstring)
            "psychic" -> colorString = context.getString(R.string.pokemon_type_psychic_hexstring)
            "bug" -> colorString = context.getString(R.string.pokemon_type_bug_hexstring)
            "rock" -> colorString = context.getString(R.string.pokemon_type_rock_hexstring)
            "ghost" -> colorString = context.getString(R.string.pokemon_type_ghost_hexstring)
            "dragon" -> colorString = context.getString(R.string.pokemon_type_dragon_hexstring)
            "dark" -> colorString = context.getString(R.string.pokemon_type_dark_hexstring)
            "steel" -> colorString = context.getString(R.string.pokemon_type_steel_hexstring)
            "fairy" -> colorString = context.getString(R.string.pokemon_type_fairy_hexstring)
            else -> {
                colorString = context.getString(R.string.pokemon_type_unkown_colorstring)
            }
        }
        return colorString
    }
}