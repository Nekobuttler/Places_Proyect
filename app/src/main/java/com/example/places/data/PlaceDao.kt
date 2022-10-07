package com.example.places.data

import android.service.autofill.OnClickAction
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.places.model.Place

@Dao
interface PlaceDao {

    //CRUD

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  addPlace(place: Place)


    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  updatePlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)

    @Query (" SELECT * FROM PLACE")
    //Datos vivos que se actualizan si otra aplicacion actualiza
    fun getPlaces() : LiveData<List<Place>>

}