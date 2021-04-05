package com.deadlinehunters.pokedex.data

import androidx.lifecycle.LiveData


class PokemonRepository(private val pokemonDao: PokemonDAO) {
    suspend fun addPokemon(pokemon: Pokemon) {
        pokemonDao.addPokemon(pokemon)
    }

    fun getAllPokemon(): LiveData<List<Pokemon>> = pokemonDao.getAllPokemon()

    suspend fun updatePokemon(pokemon: Pokemon) {
        pokemonDao.updatePokemon(pokemon)
    }

    suspend fun deletePokemon(pokemonId: Int) {
        pokemonDao.deletePokemon(pokemonId)
    }
}