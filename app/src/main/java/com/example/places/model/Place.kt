package com.example.places.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "place")

data class Place(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val  name : String,
    @ColumnInfo(name = "email")
    val  email : String?,
    @ColumnInfo(name = "phone")
    val  phone : String?,
    @ColumnInfo(name = "web")
    val  web : String?,
    @ColumnInfo(name = "latitud")
    val  latitud : Double?,
    @ColumnInfo(name = "longitud")
    val  longitud : Double?,
    @ColumnInfo(name = "altura")
    val  altura : Double?,
    @ColumnInfo(name = "ruta_audio")
    val  ruta_audio : String?,
    @ColumnInfo(name = "ruta_img")
    val  ruta_img : String?




) :Parcelable
