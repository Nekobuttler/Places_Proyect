package com.example.places

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.places.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.signin.internal.SignInClientImpl
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.Principal

class MainActivity : AppCompatActivity() {

    //Definimos un objeto para acceder  la autenticacion

    //Definimos un objeto para acceder a los elementos


    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth : FirebaseAuth



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


        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        binding.btGoogle.setOnClickListener{
            googleSignIn()
        }

    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent , 5000)
    }

    private fun firebaseAuth(idToken : String){
            val credential = GoogleAuthProvider.getCredential(idToken , null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    refresh(user)
                }else{
                    refresh(null)
                }

            }
    }

    private fun OnActivityResult(requestCode: Int,resultCode:Int , data: Intent){
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode == 5000){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuth(account.idToken!!)
            }catch (e:ApiException){

            }

        }
    }

    private fun SignIn() {

        //Recupero la informacion que le usuario escribio
        val email= binding.etEmail.text.toString()
        val password= binding.etPassword.text.toString()

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

        val email= binding.etEmail.text.toString()
        val password= binding.etPassword.text.toString()


        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                task -> if(task.isSuccessful){
                    Log.d("AUTENTICANDO ", "se autentico")
                    val user = auth.currentUser
                refresh(user)
                } else{

                Toast.makeText(baseContext, "Fallo",Toast.LENGTH_LONG).show()
                refresh(null)
                }

            }
    }

public override fun onStart(){

    super.onStart()
    val user = auth.currentUser
    refresh(user)
}


}