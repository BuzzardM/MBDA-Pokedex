package com.deadlinehunters.pokedex.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonResult (val name: String, val url: String) : Parcelable

data class Pokemon (val id: Int, val name: String, val height: Int, val weight: Int, val stats: List<PokemonStats>, val types: List<PokemonTypes>);

data class PokemonStats (val value: Int, val name: String);

data class PokemonTypes (val slot: Int, val typeName: String);