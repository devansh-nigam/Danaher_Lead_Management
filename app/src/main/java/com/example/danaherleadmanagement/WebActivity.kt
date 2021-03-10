package com.example.danaherleadmanagement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.danaherleadmanagement.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        val url=intent.getStringExtra("url")
        binding.webView.webViewClient= WebViewClient()
        binding.webView.apply{
            loadUrl(url.toString())
            settings.javaScriptEnabled=true
            //settings.safeBrowsingEnabled=true
        }
    }
}