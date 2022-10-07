package com.example.places.ui.place
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.places.R
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.databinding.FragmentUpdatePlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel

class UpdatePlaceFragment : Fragment() {


    //se recupera un argumento pasado

    private val args by navArgs<UpdatePlaceFragmentArgs>()

    private var _binding: FragmentUpdatePlaceBinding? = null

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
        _binding = FragmentUpdatePlaceBinding.inflate(inflater, container, false)
        //val root: View = binding.root


        //Pasar valores al fragment
        binding.etName.setText(args.place.name)
        binding.etEmailPlace.setText(args.place.email)
        binding.etAddPhone.setText(args.place.phone)
        binding.etWeb.setText(args.place.web)


        binding.btUpdatePlace.setOnClickListener{
            updatePlace()
        }


        return binding.root
    }

    //Registro de los datos en la base de datos
    private fun updatePlace() {
        val name = binding.etName.text.toString()
        val email = binding.etEmailPlace.text.toString()
        val phone = binding.etAddPhone.text.toString()
        val web = binding.etWeb.text.toString()

        if(name.isNotEmpty()){//Al menos se tiene un nombre
                val place = Place(args.place.id, name,email , phone , web,args.place.latitud,args.place.longitud,args.place.altura,args.place.ruta_audio,args.place.ruta_img)
            placeViewModel.savePlace(place)

            Toast.makeText(requireContext(),getString(R.string.msg_place_updated),Toast.LENGTH_SHORT).show()
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