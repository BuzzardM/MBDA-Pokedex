package com.deadlinehunters.pokedex.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPokemon(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon_table ORDER BY pokemonId ASC")
    fun readAllData(): LiveData<List<Pokemon>>
}