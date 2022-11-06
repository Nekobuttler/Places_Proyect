package com.example.places.ui.place
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.places.R
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddPlaceFragment : Fragment() {

    private var _binding: FragmentAddPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        //val root: View = binding.root

        binding.btAddPlace.setOnClickListener{
            addPlace()
        }

        GPSTurnOn()

        return binding.root
    }

    private fun GPSTurnOn() {
        if(requireActivity().
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
            && requireActivity().
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ){
            //Pedir autorizacion
            requireActivity().requestPermissions(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION ,
                Manifest.permission.ACCESS_FINE_LOCATION),
                105)

        }else{
            val fusedLocalClient : FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocalClient.lastLocation.addOnSuccessListener {
                location : Location? ->
                if(location != null){
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"

                }else{
                    binding.tvLatitud.text = "0.0"
                    binding.tvLongitud.text = "0.0"
                    binding.tvAltura.text = "0.0"
                }
            }
        }

    }

    //Registro de los datos en la base de datos
    private fun addPlace() {
        val name = binding.etName.text.toString()
        val email = binding.etEmailPlace.text.toString()
        val phone = binding.etAddPhone.text.toString()
        val web = binding.etWeb.text.toString()
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val altura = binding.tvAltura.text.toString().toDouble()

        if(name.isNotEmpty()){//Al menos se tiene un nombre
                val place = Place("" , name,email , phone , web,latitud,longitud,altura,"","")
            placeViewModel.savePlace(place)

            Toast.makeText(requireContext(),getString(R.string.msg_place_added),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addPlaceFragment_to_nav_place)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}