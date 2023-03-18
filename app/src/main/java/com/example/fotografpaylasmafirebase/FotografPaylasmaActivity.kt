package com.example.fotografpaylasmafirebase

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fotografpaylasmafirebase.databinding.ActivityFotografPaylasmaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.util.UUID

class FotografPaylasmaActivity : AppCompatActivity() {
    lateinit var binding: ActivityFotografPaylasmaBinding
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_fotograf_paylasma)
        binding = ActivityFotografPaylasmaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun paylas(view: View){
        //depo işlemleri
        //UUID -> universal uniqe id
        val uuid = UUID.randomUUID()//random ıdler ureten bir kod
        val gorselIsmi = "${uuid}.jpg"

        val reference = storage.reference//görselimiz nereye kaydedecegimizi boyle kaydedebiliyoruz
        val gorselRefrence = reference.child("images").child(gorselIsmi)//uuıd yapmalıyız //storage kaydedilen her foto için id uretecek

        if (secilenGorsel != null) {
            gorselRefrence.putFile(secilenGorsel!!).addOnSuccessListener {
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener {uri ->
                    val downloadUrl = uri.toString()//url uretiyor
                    val guncelKullaniciEmaili  = auth.currentUser!!.email.toString()
                    val kullaniciYorumu = binding.yorumText.toString()
                    val tarih = com.google.firebase.Timestamp.now()

                    //veri tabanı işlemleri
                    val postHashMap = hashMapOf<String,Any>() //hasmap ile bir klosr ousturuyoruz key value ile değerleri atıyoruz
                    postHashMap.put("gorselurl",downloadUrl)
                    postHashMap.put("kullaniciemail",guncelKullaniciEmaili)
                    postHashMap.put("kullaniciyorum",kullaniciYorumu)
                    postHashMap.put("tarih",tarih)
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){//ekleme yapıldıysa bitir
                            finish()//bitince haberler aktivitye dönüş yapıcak çünkü o sayfanın ıntentınde finish demedik
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()//hata varsa göster
                    }


                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }




    }

    fun gorselSec(view: View){

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //izni almamışız ve isteyeceğiz
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{//izin zaten varsa
            val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if (requestCode == 2 && resultCode == Activity.RESULT_OK && data !=null ){//result ok kullanıcı birşey sectiyse demek

       }
        if (data != null) {
            secilenGorsel = data.data//bunun için yukarı bir uri? = null tanımlamamazı lazım
        }
        if (secilenGorsel != null){

            if (Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                secilenBitmap = ImageDecoder.decodeBitmap(source)
                binding.imageView.setImageBitmap(secilenBitmap)

            }else{//27 ve aşagıyse bu asağıdaki kodla çalıştırabiliriz
                secilenBitmap  = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                binding.imageView.setImageBitmap(secilenBitmap)
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}