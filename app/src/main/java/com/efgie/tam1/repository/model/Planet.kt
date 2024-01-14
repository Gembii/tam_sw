package com.efgie.tam1.repository.model

data class Planet(
    val name: String,
    val diameter: String,
    val gravity: String,
    val population: String,
    val climate: String,
    val terrain: String,
    val url: String,
    val films: List<Film?>,
    val residents: List<Resident?>
)