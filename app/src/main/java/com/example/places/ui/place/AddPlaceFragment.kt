package com.example.places.ui.place
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.places.R
import com.example.places.databinding.FragmentAddPlaceBinding
import com.example.places.model.Place
import com.example.places.viewmodel.PlaceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles

class AddPlaceFragment : Fragment() {

    private var _binding: FragmentAddPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var placeViewModel: PlaceViewModel


    private lateinit var audioUtiles :AudioUtiles

    private lateinit var tomarFotoActivity : ActivityResultLauncher<Intent>

    private lateinit var imagenUtiles: ImagenUtiles


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        //val root: View = binding.root

        binding.btAddPlace.setOnClickListener{
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = "subiendo audio"
            binding.msgMensaje.visibility =TextView.VISIBLE
            subirAudio()
        }




        GPSTurnOn()

        audioUtiles = AudioUtiles(requireActivity(),
                        requireContext(),
                        binding.btAccion,
                        binding.btPlay,
                        binding.btDelete,
                        getString(R.string.graba_pausa),
                        "Stop audio")

        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }
        }


        imagenUtiles = ImagenUtiles(
            requireContext(),
            binding.btPhoto,
            binding.btRotaL,
            binding.btRotaR,
            binding.imagen,
            tomarFotoActivity)


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
    private fun subirAudio(){
        val audioFile = audioUtiles.audioFile
        if(audioFile.exists() && audioFile.isFile && audioFile.canRead()){
                val rutaLocal = Uri.fromFile(audioFile) //ruta del archivo local....
                val rutaNube = "placesApp/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"

            val referencia : StorageReference = Firebase.storage.reference.child(rutaNube) // referencia de firebase

            referencia.putFile(rutaLocal)
                .addOnSuccessListener {
                    referencia.downloadUrl.addOnSuccessListener {
                        val rutaAudio = it.toString()
                        subeImagen(rutaAudio)
                    }
                }.addOnFailureListener {
                    subeImagen("")
                }
        }else{
            subeImagen("")
        }
    }

    private fun subeImagen( rutaPublicaAudio  : String) {
        binding.msgMensaje.text = "Subiendo Imagen"
        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()) {
            val rutaLocal = Uri.fromFile(imagenFile) //ruta del archivo local....
            val rutaNube =
                "placesApp/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"

                val reference: StorageReference =
                    Firebase.storage.reference.child(rutaNube) // referencia de firebase

            reference.putFile(rutaLocal)
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        val rutaPublicaImagen = it.toString()
                        addPlace(rutaPublicaAudio, rutaPublicaImagen)
                    }
                }.addOnFailureListener {
                    addPlace(rutaPublicaAudio, "")
                }
        } else {
            addPlace(rutaPublicaAudio, "")
        }
    }

    //Registro de los datos en la base de datos
    private fun addPlace(rutaAudio : String , rutaImgen: String){
        val name = binding.etName.text.toString()
        val email = binding.etEmailPlace.text.toString()
        val phone = binding.etAddPhone.text.toString()
        val web = binding.etWeb.text.toString()
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val altura = binding.tvAltura.text.toString().toDouble()

        if(name.isNotEmpty()){//Al menos se tiene un nombre
                val place = Place("" , name,email , phone , web,latitud,longitud,altura,rutaAudio,rutaImgen)
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