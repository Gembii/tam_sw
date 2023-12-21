package com.efgie.tam1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efgie.tam1.repository.PlanetRepository
import com.efgie.tam1.repository.model.Planet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val planetRepository = PlanetRepository()

    private val mutablePlanetsData = MutableLiveData<UIState<List<Planet>>>()
    val immutablePlanetsData: LiveData<UIState<List<Planet>>> = mutablePlanetsData

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
}