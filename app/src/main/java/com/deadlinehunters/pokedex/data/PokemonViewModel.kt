package com.deadlinehunters.pokedex.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
}