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
import com.example.places.R
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel

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


        return binding.root
    }

    //Registro de los datos en la base de datos
    private fun addPlace() {
        val name = binding.etName.text.toString()
        val email = binding.etEmailPlace.text.toString()
        val phone = binding.etAddPhone.text.toString()
        val web = binding.etWeb.text.toString()

        if(name.isNotEmpty()){//Al menos se tiene un nombre
                val place = Place(0 , name,email , phone , web,0.0,0.0,0.0,"","")
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