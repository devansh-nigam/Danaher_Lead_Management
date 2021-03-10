package com.example.danaherleadmanagement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.danaherleadmanagement.databinding.ActivityWebBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        val settingsf = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settingsf

        val title=intent.getStringExtra("Title").toString()
        if(title=="Contact"){
            supportActionBar!!.title="Contact Us"

        }
        else if(title=="Terms"){
            supportActionBar!!.title="Terms Of Use"
        }
        else if(title=="Privacy"){
            supportActionBar!!.title="Privacy Policy"
        }

        supportActionBar!!.subtitle="Danaher"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        db.collection("URL").document("Links").get().addOnSuccessListener {
            val url=it.data!!.get(title) as String?
            binding.webView.webViewClient= WebViewClient()
            binding.webView.apply{
                loadUrl(url.toString())
                settings.javaScriptEnabled=true
            }
        }
    }
}