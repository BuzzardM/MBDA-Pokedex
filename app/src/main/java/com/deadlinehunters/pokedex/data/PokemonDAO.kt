package com.deadlinehunters.pokedex.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPokemon(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon_table ORDER BY pokemonId ASC")
    fun getAllPokemon(): LiveData<List<Pokemon>>

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)

    @Query("DELETE FROM pokemon_table WHERE id = :pokemonId")
    suspend fun deletePokemon(pokemonId: Int)
}