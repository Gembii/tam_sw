package com.efgie.tam1.repository

import com.efgie.tam1.repository.model.PlanetResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PlanetService {

    @GET("/api/planets")
    suspend fun getPlanetResponse(): Response<PlanetResponse>

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