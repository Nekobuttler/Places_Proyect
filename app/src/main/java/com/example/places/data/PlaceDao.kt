package com.example.places.data

import android.service.autofill.OnClickAction
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.places.model.Place
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

import com.google.firebase.ktx.Firebase


class PlaceDao {

    //CRUD


    //Valores para la estructura de Firestore
    private val collection1 = "placesApp"

    private val user = Firebase.auth.currentUser?.email.toString()

    private val collection2 = "MyPlaces"

    //Objeto para la conexion de la base de datos en la nube

    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance() //Se obtiene la conexion

    init {
        //Como se inicializa la conexion con Firestore
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    //Se recibe un objeto lugar, se valida si el ID tiene algo es una actualizacion de lo contrario se crea
     fun  savePlace(place: Place){
         val document : DocumentReference
        if(place.id.isEmpty()){
            document = firestore         //baja entre carpetas de usuarios y datos
                .collection(collection1)
                .document(user)
                .collection(collection2)
                .document()
            place.id = document.id
        }else{
          document =   firestore         //baja entre carpetas de usuarios y datos
                .collection(collection1)
                .document(user)
                .collection(collection2)
                .document(place.id) //<--busca el documento que tiene el id
        }
        //registrar info

        document.set(place).addOnSuccessListener {

                    Log.d("save place", "Lugar agregado/actualizado")

        }
            .addOnCanceledListener {
                Log.e("save place" , "Lugar NO FUE agregado/actualizado")
            }



     }


     fun deletePlace(place: Place){

         //se valida si el id tiene algo

         if(place.id.isNotEmpty()){
             firestore         //baja entre carpetas de usuarios y datos
                 .collection(collection1)
                 .document(user)
                 .collection(collection2)
                 .document(place.id).delete().addOnSuccessListener {

                     Log.d("delete place", "Lugar delete/delete")

                 }
                 .addOnCanceledListener {
                     Log.e("delete place" , "Lugar NO FUE delete/delete")
                 }
         }

     }


    fun getPlaces() : MutableLiveData<List<Place>>{
        val listPlaces = MutableLiveData<List<Place>>()
        firestore         //baja entre carpetas de usuarios y datos
            .collection(collection1)
            .document(user)
            .collection(collection2)
            .addSnapshotListener{   // --> SnapChat: Vista del estado actual de algo
                instant , error ->
                if(error != null){ //Si hay un error en la generacion
                    return@addSnapshotListener
                }
                if (instant !=null ){ //Si estamos aca entocne sno hubo errores
                //hay datos en la instantanea
                    val list = ArrayList<Place>()
                    //Se recorre la instant para transformar cada isntant en un objeto lugar

                    instant.documents.forEach{   //it es el objeto de lambda
                        val place = it.toObject( Place::class.java)
                        if(place != null){
                            list.add(place)
                        }
                    }
                    listPlaces.value = list


                }



            }



        return listPlaces
    }

}