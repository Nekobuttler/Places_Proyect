package com.example.places.repository

import androidx.lifecycle.LiveData

import com.example.places.data.PlaceDao
import com.example.places.model.Place

class PlaceRepository (private val placeDao : PlaceDao){

    suspend fun  savePlace(place: Place){
      placeDao.savePlace(place)

    }



     fun deletePlace(place: Place){
        placeDao.deletePlace(place)
    }


    val getPlaces : LiveData<List<Place>> = placeDao.getPlaces()
}