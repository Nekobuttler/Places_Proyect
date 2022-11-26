package com.example.places.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.places.databinding.FragmentGalleryBinding
import com.example.places.model.Place
import com.example.places.viewmodel.GalleryViewModel
import com.example.places.viewmodel.PlaceViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GalleryFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    //Este objeto sera para interactuar con el mapa de la vista

    private lateinit var googleMap : GoogleMap

    private var mapReady =  false

    //Se toman lso datos de los lugares desde PlaceViewModel

    private lateinit var placeViewModel: PlaceViewModel

    //Funcion especial que se ejecuta al crear el Activity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Se solicita la actualizacion del mapa
        //desplegando en la pantalla los lugares

        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync (this@GalleryFragment)

    }

    //Cuando el mapa ya esta lista para mostrarse

    override fun onMapReady(map: GoogleMap) {
        map.let{
            googleMap = it
            mapReady = true

            //Se instruye al mapa para que se acutalize la info de los lugares segun la vista

            placeViewModel.getPlaces.observe(viewLifecycleOwner){
                places ->
                updateMap(places)

            }

        }
    }

    private fun updateMap(places: List<Place>?) {
        if(mapReady) {
            places?.forEach {
            place ->
                if(place.latitud?.isFinite() == true
                    &&
                    place.longitud?.isFinite() == true){
                    val marca = LatLng(place.latitud,place.longitud)
                    googleMap.addMarker(MarkerOptions().position(marca).title("${place.name}"))
                }

        }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val placeViewModel =
            ViewModelProvider(this)[PlaceViewModel :: class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}