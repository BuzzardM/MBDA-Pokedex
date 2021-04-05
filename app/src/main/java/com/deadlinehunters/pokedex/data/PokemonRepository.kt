package com.deadlinehunters.pokedex.data

import androidx.lifecycle.LiveData

class PokemonRepository(private val pokemonDao: PokemonDAO) {
    val readAllData: LiveData<List<Pokemon>> = pokemonDao.readAllData()

    suspend fun addPokemon(pokemon: Pokemon){
        pokemonDao.addPokemon(pokemon)
    }
}