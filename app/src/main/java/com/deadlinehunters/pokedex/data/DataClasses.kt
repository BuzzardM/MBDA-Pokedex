package com.deadlinehunters.pokedex.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonResult (val name: String, val url: String) : Parcelable

@Entity(tableName = "pokemon_table")
data class Pokemon (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pokemonId: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: Map<String, Int>,
    val types: Map<Int, String>,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var background: ByteArray?)