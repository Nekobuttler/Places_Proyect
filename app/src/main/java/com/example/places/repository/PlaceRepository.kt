package com.example.places.repository

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.places.data.PlaceDao
import com.example.places.model.Place

class PlaceRepository (private val placeDao : PlaceDao){

    suspend fun  savePlace(place: Place){
        if(place.id == 0 ){
            placeDao.addPlace(place)
        }else{
            placeDao.updatePlace(place)
        }

    }

    suspend fun  updatePlace(place: Place){
        placeDao.updatePlace(place)
    }


    suspend fun deletePlace(place: Place){
        placeDao.deletePlace(place)
    }


    val getPlaces : LiveData<List<Place>> = placeDao.getPlaces()
}