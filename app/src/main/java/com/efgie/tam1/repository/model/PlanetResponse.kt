package com.efgie.tam1.repository.model
data class PlanetResponse(
    val name: String,
    val diameter: String,
    val gravity: String,
    val population: String,
    val climate: String,
    val terrain: String,
    val url: String,
    val films: List<String>,
    val residents: List<String>
    )