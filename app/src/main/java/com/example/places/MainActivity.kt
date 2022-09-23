package com.example.places

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.places.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.Principal

class MainActivity : AppCompatActivity() {

    //Definimos un objeto para acceder  la autenticacion

    private lateinit var auth : FirebaseAuth

    //Definimos un objeto para acceder a los elementos

    private lateinit var binding: ActivityMainBinding

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Inicializar la autenticacion

        FirebaseApp.initializeApp(this)

        auth = Firebase.auth

        //Definimos los eventos Onclick

        binding.btSignin.setOnClickListener{
            SignIn()
        }

        binding.btLogin.setOnClickListener{
            LogIn()
        }



    }

    private fun SignIn() {

        //Recupero la informacion que le usuario escribio
        val email= binding.etPassword.text.toString()
        val password= binding.etEmail.text.toString()

        //Crear el usuario
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val user= auth.currentUser
                    refresh(user)
                }else{
                    Toast.makeText(baseContext,"Fallo",Toast.LENGTH_LONG).show()
                    refresh(null)
                }
            }
    }

    private fun refresh(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)

        }else{

        }
    }

    private fun LogIn() {

    }
}