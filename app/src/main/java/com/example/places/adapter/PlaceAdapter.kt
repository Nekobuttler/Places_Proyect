package com.example.places.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.databinding.PlaceRowBinding
import com.example.places.model.Place
import com.example.places.ui.place.PlaceFragmentDirections

class PlaceAdapter : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

    //Lista de lugares a dibujar

    private var PlacesList = emptyList<Place>()


        //Contenedor de vistas en memoria del objeto
        inner class PlaceViewHolder( private val itemBinding: PlaceRowBinding)
            : RecyclerView.ViewHolder(itemBinding.root) //Recibe una lista de lo creado anteriormente
        {

                fun GetData(place : Place){
                    itemBinding.tvName.text = place.name
                    itemBinding.tvCellphone.text = place.phone
                    itemBinding.tvCorreo.text = place.email
                    Glide.with(itemBinding.root.context)
                        .load(place.ruta_img)
                        .circleCrop()
                        .into(itemBinding.imageView2)
                    itemBinding.vistaRow.setOnClickListener {
                        val accion = PlaceFragmentDirections.
                                        actionNavPlaceToUpdatePlaceFragment(place)

                        itemView.findNavController().navigate(accion)
                    }

                }
        }

    //Crea una vista(caja) del tipo placeRow
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemBinding = PlaceRowBinding
                    .inflate(LayoutInflater.from(parent.context)
                    ,parent,
                    false)

        return PlaceViewHolder(itemBinding)
    }

    //Con la vista creada llenara los datos dentro de la caja
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val actualPlace = PlacesList[position]
        holder.GetData(actualPlace)
    }

    override fun getItemCount(): Int {
        return PlacesList.size
    }

    fun setPlaces( places : List<Place>){
        PlacesList = places

        notifyDataSetChanged() // se notifica que el conjunto de datos cambio y se redibuja toda la lista
    }


}