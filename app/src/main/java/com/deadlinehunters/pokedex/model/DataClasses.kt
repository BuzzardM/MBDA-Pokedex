package com.deadlinehunters.pokedex.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PokemonResult (val name: String, val url: String) : Parcelable

data class Pokemon (val id: Int, val name: String, val height: Int, val weight: Int, val stats: Map<String, Int>, val types: Map<Int, String>)