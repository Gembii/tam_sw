package com.efgie.tam1.repository
import com.efgie.tam1.repository.model.PlanetResponse
import retrofit2.Response

class PlanetRepository {

    suspend fun getPlanetResponse(): Response<PlanetResponse> =
        PlanetService.planetService.getPlanetResponse()

}