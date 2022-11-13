package com.example.places.ui.place
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AsyncPlayer
import android.media.MediaPlayer
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.places.R
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.databinding.FragmentUpdatePlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel
import java.net.URI
import java.util.logging.Level.parse

class UpdatePlaceFragment : Fragment() {


    //se recupera un argumento pasado

    private val args by navArgs<UpdatePlaceFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

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

        binding.tvLatitud.text = args.place.latitud.toString()
        binding.tvLongitud.text = args.place.longitud.toString()
        binding.tvAltura.text = args.place.altura.toString()

        binding.btAddPlace.setOnClickListener {
            updatePlace()
        }

        binding.btDelete.setOnClickListener {
            deletePlace()
        }
        binding.btEmail.setOnClickListener {
            sendEmail()
        }
        binding.btPhone.setOnClickListener {
            callPlace()
        }
        binding.btWhatsapp.setOnClickListener {
            sendMessage()
        }
        binding.btWeb.setOnClickListener {
            seeWeb()
        }
        binding.btLocation.setOnClickListener {
            seeMap()
        }


        if (args.place.ruta_audio?.isNotEmpty() == true) {
            //Activamos boton para escuchar audio
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.place.ruta_audio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
        } else {
            binding.btPlay.isEnabled = false

        }

        binding.btPlay.setOnClickListener {
        mediaPlayer.start()
    }

    if(args.place.ruta_img?.isNotEmpty() == true){
        Glide.with(requireContext())
            .load(args.place.ruta_img)
            .fitCenter()
            .into(binding.imagen)
    }

        return binding.root
    }

    private fun sendEmail() {
        val value  = binding.etEmailPlace.text.toString()
        if(value.isNotEmpty()){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="message/rfc882"
            intent.putExtra(Intent.EXTRA_EMAIL , arrayOf(value))
            intent.putExtra(Intent.EXTRA_SUBJECT ,
            getString(R.string.msg_saludos) + " "
                    + binding.etName.text)
            intent.putExtra(Intent.EXTRA_TEXT ,
                getString(R.string.msg_mensaje_correo))
            startActivity(intent)

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun seeWeb() {
        val value  = binding.etWeb.text.toString()
        if(value.isNotEmpty()){

            val uri = "http://$value"
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(uri))
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun seeMap() {
        val latitud  = binding.tvLatitud.text.toString().toDouble()
        val longitud  = binding.tvLongitud.text.toString().toDouble()
        val altura  = binding.tvAltura.text.toString().toDouble()
        if(latitud.isFinite() && longitud.isFinite()){

            val uri = "geo:$latitud,$longitud?z18 "
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(uri))
            startActivity(intent)


        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun callPlace() {
        val value  = binding.etAddPhone.text.toString()
        if(value.isNotEmpty()){
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$value")
            if(requireActivity().
                checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED){
                //Pedir autorizacion
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 105)
            }else{
                requireActivity().startActivity(intent)
            }
            startActivity(intent)


        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun sendMessage() {
        val value  = binding.etAddPhone.text.toString()
        if(value.isNotEmpty()){
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=506$value&text="+getString(R.string.msg_saludos)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri)
            startActivity(intent)


        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun deletePlace() {
        //Ventana de dialogo

        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle(R.string.bt_delete_place)
        alert.setMessage(getString(R.string.msg_question) + "${args.place.name}?")
        alert.setPositiveButton(getString(R.string.msg_yes)) {
            _,_ ->
                placeViewModel.deletePlace(args.place)
                Toast.makeText(requireContext(),getString(R.string.msg_deleted_place) , Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_updatePlaceFragment_to_nav_place)
        }
        alert.setNegativeButton(getString(R.string.msg_no)){
            _,_-> }
        alert.create().show()
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
            findNavController().navigate(R.id.action_updatePlaceFragment_to_nav_place)
        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}