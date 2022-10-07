package com.example.places.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.R
import com.example.places.adapter.PlaceAdapter
import com.example.places.databinding.FragmentPlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel

class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        placeViewModel =ViewModelProvider(this).get(PlaceViewModel::class.java)
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        //val root: View = binding.root

        binding.addPlaceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addPlaceFragment_to_nav_place)
        }


        //ACTIVAR EL RECYCLER VIEW usando el adapter

        val placeAdapter = PlaceAdapter()

        val recycler = binding.recycler
        recycler.adapter = placeAdapter

        recycler.layoutManager = LinearLayoutManager(requireContext())

        placeViewModel.getPlaces.observe(viewLifecycleOwner) {
            places -> placeAdapter.setPlaces(places)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}