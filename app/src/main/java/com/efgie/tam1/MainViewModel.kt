package com.efgie.tam1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efgie.tam1.repository.PlanetRepository
import com.efgie.tam1.repository.model.Planet
import com.efgie.tam1.repository.model.PlanetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val planetRepository = PlanetRepository()

    private val mutablePlanetsData = MutableLiveData<UIState<List<PlanetResponse>>>()
    val immutablePlanetsData: LiveData<UIState<List<PlanetResponse>>> = mutablePlanetsData

    private val mutablePlanetDetails = MutableLiveData<UIState<Planet>>()
    val immutablePlanetDetails: LiveData<UIState<Planet>> = mutablePlanetDetails

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = planetRepository.getPlanetResponse()
                val planets = response.body()?.results
                mutablePlanetsData.postValue(UIState(data = planets, isLoading = false, error = null))

            } catch (e: Exception) {
                mutablePlanetsData.postValue(UIState(data = null, isLoading = false, error = e.message))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }

    fun getDetailsData(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val responsePlanet = planetRepository.getPlanetById(id)
                val planetDetails = responsePlanet.body()

                if (planetDetails != null) {
                    val films = planetDetails.films.map { url -> planetRepository.getPlanetFilm(url).body() }
                    val residents = planetDetails.residents.map { url -> planetRepository.getPlanetResident(url).body() }

                    val completePlanetDetails = Planet(planetDetails.name, planetDetails.diameter, planetDetails.gravity, planetDetails.population, planetDetails.climate, planetDetails.terrain, planetDetails.url, films, residents)

                    mutablePlanetDetails.postValue(UIState(data = completePlanetDetails, isLoading = false, error = null))
                } else {
                    mutablePlanetDetails.postValue(UIState(data = null, isLoading = false, error = "Planet details not found"))
                }
            } catch (e: Exception) {
                mutablePlanetDetails.postValue(UIState(data = null, isLoading = false, error = e.message))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }
}