package com.example.places.model

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize


data class Place(


    var id: String,

    val  name : String,

    val  email : String?,

    val  phone : String?,

    val  web : String?,

    val  latitud : Double?,

    val  longitud : Double?,

    val  altura : Double?,

    val  ruta_audio : String?,

    val  ruta_img : String?




) :Parcelable{
    constructor():
            this("",
                "",
                "",
                "",
                "",
                0.00,
                0.00,
                0.00,
                "",
                "")

}
