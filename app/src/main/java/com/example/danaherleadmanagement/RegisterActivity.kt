package com.example.danaherleadmanagement

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.danaherleadmanagement.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setHomeButtonEnabled(true)

        val operatingSpinner = findViewById<Spinner>(R.id.spinnerOperating)
        operatingSpinner.isVisible=false
        binding.passwd2.isVisible=false
        binding.warning.isVisible=false
        binding.progressBar.isVisible=false

        mAuth = FirebaseAuth.getInstance()
        //val user:FirebaseUser? = mAuth.currentUser
        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings


        var domain=""
        var operatingCompany=""
        var full_name=""
        var email=""
        var pass1=""
        var pass2=""

        var flagName=1
        var flagNameValidate=1
        var flagEmail=1
        var flagEmailValidate=1
        var flagDomain=1
        var flagOpCo=1
        var flagPassValidate=1
        var flagPass1=1
        var flagPass2=1
        var flagPassMatch=1
        var flagCheck=1


        val domainSpinner = findViewById<Spinner>(R.id.spinnerDomain)
        val adapter = DomainArrayAdapter(this, EnterpriseDomain.EnterpriseDomains.list!!)
        domainSpinner.adapter=adapter

        domainSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

                val selectedItem = parent!!.getItemAtPosition(position)
                val s = selectedItem.toString()
                domain = s.substring(s.lastIndexOf('=') + 1)
                domain = domain.substring(0, domain.indexOf(')'))

                if(domain=="Life Sciences"){
                    operatingSpinner.isVisible=true
                    flagDomain=0
                    val adapterLifeScience  = LifeScienceArrayAdapter(this@RegisterActivity, LifeScience.LifeSciences.list!!)
                    operatingSpinner.adapter=adapterLifeScience
                }
                else if(domain=="Environmental & Applied Solutions")
                {
                    flagDomain=0
                    operatingSpinner.isVisible=true
                    val adapterEnvironmental  = EnvironmentalArrayAdapter(this@RegisterActivity, Environmental.Environmentals.list!!)
                    operatingSpinner.adapter=adapterEnvironmental
                }
                else if(domain=="Diagnostics"){
                    operatingSpinner.isVisible=true
                    flagDomain=0
                    val adapterDiagnostic  = DiagnosticArrayAdapter(this@RegisterActivity, Diagnostic.Diagnostics.list!!)
                    operatingSpinner.adapter=adapterDiagnostic
                }
                else{
                    flagDomain=1
                    operatingSpinner.isVisible=false
                }

                if(flagDomain==0)
                {
                    operatingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long) {
                            if(position!=0)
                            {
                            flagOpCo=0} else flagOpCo=1
                            val selectedCompany = parent!!.getItemAtPosition(position)
                            val sC = selectedCompany.toString()
                            operatingCompany = sC.substring(sC.lastIndexOf('=') + 1)
                            operatingCompany = operatingCompany.substring(0, operatingCompany.indexOf(')'))
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            flagOpCo = 1
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //flagBranch = 1
                flagDomain=1
            }
        }


        binding.checkBox.setOnClickListener {
            if(binding.checkBox.isChecked)flagCheck=0
            else flagCheck=1
        }

        binding.toLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toTerms.setOnClickListener {
            val intent=Intent(this,WebActivity::class.java)
            intent.putExtra("Title","Terms")
            startActivity(intent)
        }

        binding.register.setOnClickListener{
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }

            full_name=binding.fname.text.toString().trim()

            if(full_name.isEmpty())flagName=1
            else {
                flagName = 0
                flagNameValidate=NameValidate(full_name)
            }

            email=binding.email.text.toString().trim()

            if(email.isEmpty())flagEmail=1
            else {
                flagEmail = 0
                flagEmailValidate=EmailValidator(email)
            }

            pass1=binding.passwd1.text.toString()

            if(pass1.isEmpty())flagPass1=1
            else{
                flagPass1=0
                flagPassValidate=PasswordValidator(pass1)
                if(flagPassValidate==0){
                    binding.warning.isVisible=false
                    binding.passwd2.isVisible=true
                }
                else{
                    binding.passwd2.isVisible=false
                    binding.warning.isVisible=true
                }
            }

            pass2=binding.passwd2.text.toString()
            if(pass2.isEmpty())flagPass2=1
            else{
                flagPass2=0
                if(pass1.equals(pass2))flagPassMatch=0
                else flagPassMatch=1
            }


            if(flagName==1){
                val snack = Snackbar.make(it,"Please Enter Your Full Name",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagNameValidate==1){
                val snack = Snackbar.make(it,"Please Enter Valid Full Name",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagEmail==1){
                val snack = Snackbar.make(it,"Please Enter Your Enterprise Email",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagEmailValidate==1){
                val snack = Snackbar.make(it,"Please Enter A Valid Email",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagDomain==1){
                val snack = Snackbar.make(it,"Please Select Your Company Domain",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagOpCo==1){
                val snack = Snackbar.make(it,"Please Select Your Operating Company",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagPass1==1){
                val snack = Snackbar.make(it,"Please Create A Password",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagPassValidate==0 && flagPass2==1){
                val snack = Snackbar.make(it,"Please Confirm Your Password",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagPassValidate==0 && flagPass2==0 && flagPassMatch==1){
                val snack = Snackbar.make(it,"Passwords don't match, Please Try Again",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagPassValidate==0 && flagPass2==0 && flagCheck==1){
                val snack = Snackbar.make(it,"Please Agree To The Terms",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                progressBar.isVisible=true
                binding.register.isClickable=false
                mAuth.createUserWithEmailAndPassword(email,pass1).
                        addOnCompleteListener({task->
                            if(task.isSuccessful){
                                binding.register.isClickable=false

                                val user=mAuth.currentUser!!

                                val userDetails = hashMapOf(
                                    "Name" to "${full_name}",
                                    "Email" to "${user.email}",
                                    "Domain" to domain,
                                    "OpCo" to operatingCompany,
                                    "UID" to user.uid,
                                    "TimestampRegistration" to Timestamp.now().toDate().toString())

                                db.collection("Users").document(user.email).collection("Account Info").document("Details").set(userDetails)
                                    .addOnCompleteListener {
                                        Toast.makeText(this,"User registered successfully",Toast.LENGTH_LONG).show()
                                        val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }.addOnFailureListener {
                                        binding.register.isClickable=true
                                        Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                    }
                            }else{
                                binding.register.isClickable=true
                                Toast.makeText(this,"${task.exception!!.message}",Toast.LENGTH_LONG).show()
                                binding.progressBar.isVisible=false
                            }
                        })

            }
        }
    }

    private fun PasswordValidator(pass1: String): Int {
        var upper = false
        var lower = false
        var digit = false
        var special = false

        if(pass1.length<6)return 1

        for(i in pass1){
            if(i.isDigit())digit=true
            else if(i.isUpperCase())upper=true
            else if(i.isLowerCase())lower=true
            else special=true
        }
        if(upper && lower && digit && special)
            return 0
        else return 1
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

    private fun NameValidate(fullName: String): Int {

        var result=0
        for(i in fullName){
            if(i.isDigit()){
                result=1
                break;
            }
        }
        return result
    }
}