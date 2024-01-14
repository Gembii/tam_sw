package com.efgie.tam1.repository.model

data class PlanetsResponse(
    val count: Int,
    val results: List<PlanetResponse>
)