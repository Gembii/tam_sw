package com.efgie.tam1.repository
import com.efgie.tam1.repository.model.Film
import com.efgie.tam1.repository.model.Planet
import com.efgie.tam1.repository.model.PlanetResponse
import com.efgie.tam1.repository.model.PlanetsResponse
import com.efgie.tam1.repository.model.Resident
import retrofit2.Response

class PlanetRepository {

    suspend fun getPlanetResponse(): Response<PlanetsResponse> =
        PlanetService.planetService.getPlanetResponse()

    suspend fun getPlanetById(id: String): Response<PlanetResponse> =
        PlanetService.planetService.getPlanetById(id)

    suspend fun getPlanetFilm(url: String): Response<Film> =
        PlanetService.planetService.getPlanetFilm(url)

    suspend fun getPlanetResident(url: String): Response<Resident> =
        PlanetService.planetService.getPlanetResident(url)
}