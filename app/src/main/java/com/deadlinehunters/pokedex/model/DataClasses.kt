package com.deadlinehunters.pokedex.model

data class PokemonResult (val name: String, val url: String);

data class Pokemon (val id: Int, val name: String, val height: Int, val weight: Int, val stats: List<PokemonStats>, val types: List<PokemonTypes>);

data class PokemonStats (val value: Int, val name: String);

data class PokemonTypes (val slot: Int, val typeName: String);