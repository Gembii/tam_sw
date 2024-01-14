package com.efgie.tam1.repository

import com.efgie.tam1.repository.model.Film
import com.efgie.tam1.repository.model.Planet
import com.efgie.tam1.repository.model.PlanetResponse
import com.efgie.tam1.repository.model.PlanetsResponse
import com.efgie.tam1.repository.model.Resident
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PlanetService {

    @GET("/api/planets")
    suspend fun getPlanetResponse(): Response<PlanetsResponse>

    @GET("/api/planets/{id}")
    suspend fun getPlanetById(@Path("id") planetId: String): Response<PlanetResponse>

    @GET
    suspend fun getPlanetFilm(@Url url: String): Response<Film>

    @GET
    suspend fun getPlanetResident(@Url url: String): Response<Resident>

    companion object {
        private const val API_URL = "https://swapi.dev/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val planetService: PlanetService by lazy {
            retrofit.create(PlanetService::class.java)
        }
    }
}