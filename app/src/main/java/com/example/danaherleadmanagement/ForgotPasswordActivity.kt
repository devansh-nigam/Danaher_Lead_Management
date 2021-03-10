package com.example.danaherleadmanagement

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.danaherleadmanagement.databinding.ActivityForgotPasswordBinding
import com.example.danaherleadmanagement.databinding.ActivityLeadSubmissionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title="Forgot Password"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mAuth= FirebaseAuth.getInstance()
        binding.progressBar.isVisible=false
        var email=""
        var flagEmail=1
        var flagEmailValidate=1

        binding.back.setOnClickListener {
            BackToLogin()
        }

        binding.reset.setOnClickListener {

            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }

            email=binding.email.text.toString()

            if(email.isEmpty())flagEmail=1
            else {
                flagEmail = 0
                flagEmailValidate=EmailValidator(email)
            }

            if(flagEmail==1){
                val snack = Snackbar.make(it,"Please Enter Your Enterprise Email", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagEmailValidate==1){
                val snack = Snackbar.make(it,"Please Enter A Valid Email", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                binding.reset.isClickable=false
                progressBar.isVisible=true
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(this,"Password Reset Link Sent Successfully!",Toast.LENGTH_LONG).show()
                    BackToLogin()
                }.addOnFailureListener {
                    progressBar.isVisible=false
                    binding.reset.isClickable=true
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun BackToLogin(){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun EmailValidator(email: String): Int {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        if(EMAIL_ADDRESS_PATTERN.matcher(email).matches())return 0
        else return 1
    }
}