package com.example.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.fotografpaylasmafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class KullaniciActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()

        val guncelKullanici = auth.currentUser
        if (guncelKullanici !=null){//kullanıcı zaten giriş yapmıs
            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }





    }
    fun girisYap(view: View){

        auth.signInWithEmailAndPassword(binding.emailText.text.toString(),binding.passwordText.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldiniz: ${guncelKullanici}",Toast.LENGTH_LONG).show()

                val intent = Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.addOnFailureListener { exception->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }
    fun kayitOl(view : View){
        val email = binding.emailText.text.toString()
        val sifre = binding.passwordText.text.toString()
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener{task ->
        //asenkron
        if (task.isSuccessful){
            //diğer aktiviteye gidelim
            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }

        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

        }


    }
}
