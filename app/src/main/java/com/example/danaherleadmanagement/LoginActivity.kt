package com.example.danaherleadmanagement

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.danaherleadmanagement.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        val user=mAuth.currentUser

        if(user!=null){
            val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        var email=""
        var pass=""
        var flagEmail=1
        var flagEmailValidate=1
        var flagPass=1

        binding.showPassword.setOnClickListener {
            if(binding.showPassword.isChecked) binding.passwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else binding.passwd.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        binding.newRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.forgot.setOnClickListener {
            val intent=Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
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

            pass=binding.passwd.text.toString()

            if(pass.isEmpty())flagPass=1
            else{flagPass=0}


             if(flagEmail==1){
            val snack = Snackbar.make(it,"Please Enter Your Enterprise Email", Snackbar.LENGTH_LONG)
            snack.show()
            }
        else if(flagEmailValidate==1){
            val snack = Snackbar.make(it,"Please Enter A Valid Email", Snackbar.LENGTH_LONG)
            snack.show()
        }
             else if(flagPass==1){
                 val snack = Snackbar.make(it,"Please Enter Your Password",Snackbar.LENGTH_LONG)
                 snack.show()
             }
            else{
                 var pd= ProgressDialog(this)
                 pd.setTitle("Loading...")
                 pd.setMessage("Logging You In")
                 pd.show()
                mAuth.signInWithEmailAndPassword(email,pass).
                        addOnCompleteListener(
                                {
                                    task->
                                    if(task.isSuccessful){
                                        val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        pd.hide()
                                        Toast.makeText(this,"${task.exception!!.message}",Toast.LENGTH_LONG).show()
                                    }
                                }
                        )
            }

        }
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