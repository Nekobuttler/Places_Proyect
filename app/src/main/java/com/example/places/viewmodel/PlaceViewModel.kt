package com.example.places.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.places.data.PlaceDao
import com.example.places.data.PlaceDatabase
import com.example.places.model.Place
import com.example.places.repository.PlaceRepository
import kotlinx.coroutines.launch

class PlaceViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: PlaceRepository = PlaceRepository(PlaceDao())

    val getPlaces = repository.getPlaces

    fun savePlace(place: Place) {
        viewModelScope.launch {
            repository.savePlace(place)
        }
    }

    /*
    suspend fun updatePlace(place: Place) {
        viewModelScope.launch {
            repository.updatePlace(place)
        }
    }
    */


    suspend fun deletePlace(place: Place) {
        viewModelScope.launch {
            repository.deletePlace(place)
        }
    }

}