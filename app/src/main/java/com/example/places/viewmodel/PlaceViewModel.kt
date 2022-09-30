package com.example.places.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.places.data.PlaceDao
import com.example.places.data.PlaceDatabase
import com.example.places.model.Place
import com.example.places.repository.PlaceRepository
import kotlinx.coroutines.launch

class PlaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository

    val getPlaces: LiveData<List<Place>>


    init {
        val placeDao = PlaceDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(placeDao)
        getPlaces = repository.getPlaces

    }

    suspend fun addPlace(place: Place) {
        viewModelScope.launch {
            repository.addPlace(place)
        }
    }

    suspend fun updatePlace(place: Place) {
        viewModelScope.launch {
            repository.updatePlace(place)
        }
    }


    suspend fun deletePlace(place: Place) {
        viewModelScope.launch {
            repository.deletePlace(place)
        }
    }

}