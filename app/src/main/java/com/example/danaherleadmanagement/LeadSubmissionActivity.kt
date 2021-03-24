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
import com.example.danaherleadmanagement.databinding.ActivityLeadSubmissionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.security.MessageDigest
import java.util.regex.Pattern

class LeadSubmissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeadSubmissionBinding
    private lateinit var mAuth: FirebaseAuth

    val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLeadSubmissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth= FirebaseAuth.getInstance()

        db.firestoreSettings = settings

        binding.progressBar.isVisible=false

        var full_name=""
        var email=""
        var opportunity=""
        var segment=""
        var value=""

        var flagName=1
        var flagNameValidate=1
        var flagEmail=1
        var flagEmailValidate=1
        var flagOpportunity=1
        var flagSegment=1
        var flagValue=1
        var flagCheck=1
        var sameEmail=1

        val currentUser=mAuth.currentUser
        binding.emailOfSender.text=currentUser.email


        binding.checkBox.setOnClickListener {
            if(binding.checkBox.isChecked)flagCheck=0
            else flagCheck=1
        }

        binding.submit.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }

            binding.progressBar.isVisible=false

            full_name=binding.customerName.text.toString().trim()

            if(full_name.isEmpty())flagName=1
            else {
                flagName = 0
                flagNameValidate=NameValidate(full_name)
            }

            email=binding.emailOfReceiver.text.toString().trim()

            if(email.isEmpty())flagEmail=1
            else {
                flagEmail = 0
                flagEmailValidate=EmailValidator(email)
                if(flagEmailValidate==0){
                    if(currentUser.email==email){
                        sameEmail=1
                    }else sameEmail=0
                }
            }

            opportunity=binding.opportunity.text.toString().trim()

            if(opportunity.isEmpty())flagOpportunity=1
            else {
                flagOpportunity = 0
            }

            segment=binding.segmentDetails.text.toString().trim()

            if(segment.isEmpty())flagSegment=1
            else {
                flagSegment = 0
            }

            value=binding.value.text.toString().trim()

            if(value.isEmpty())flagValue=1
            else {
                flagValue = 0
            }



            if(flagName==1){
                val snack = Snackbar.make(it,"Please Enter Lead's Name", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagNameValidate==1){
                val snack = Snackbar.make(it,"Please Enter Valid Full Name", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagOpportunity==1){
                val snack = Snackbar.make(it,"Please Enter Opportunity Details", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagSegment==1){
                val snack = Snackbar.make(it,"Please Enter Segment Details", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagValue==1){
                val snack = Snackbar.make(it,"Please Enter Value Of Lead in $", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagEmail==1){
                val snack = Snackbar.make(it,"Please Enter Email For Whom This Lead Is Assigned", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagEmailValidate==1){
                val snack = Snackbar.make(it,"Please Enter A Valid Email", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(sameEmail==1){
                val snack = Snackbar.make(it,"Oops! You Cannot Forward A Lead To Yourself!", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagCheck==1){
                val snack = Snackbar.make(it,"Please Check The Undertaking", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                binding.progressBar.isVisible=true
                binding.submit.isClickable=false
                binding.submit.isVisible=false

                val now=Timestamp.now()
                val bytes= MessageDigest.getInstance("MD5").digest(now.toString().toByteArray())
                val hash=bytes.joinToString("") { "%02x".format(it) }

                val leadDetails = hashMapOf(
                    "Name" to full_name,
                    "Opportunity" to opportunity,
                    "Segment" to segment,
                    "Value" to "$$value",
                    "SubmittedBy" to currentUser!!.email,
                    "SubmittedTo" to email,
                    "Status" to "Open",
                    "TimestampSubmission" to now.toDate().toString(),
                    "TimestampValidated" to "",
                    "TimestampRejected" to "",
                    "TimestampClosed" to "",
                        "TimestampLatest" to now.toDate().toString(),
                    "Hash" to hash
                )

                val finalCheck=checkForUser(email)
                if(finalCheck)
                {
                    //adding as assigned lead first
                    db.collection("Users").document(email).collection("Assigned Leads").document(hash).set(leadDetails)
                            .addOnSuccessListener {
                                db.collection("Users").document(currentUser.email!!).collection("Submitted Leads").document(hash).set(leadDetails)
                                        .addOnSuccessListener {

                                            Toast.makeText(this,"Lead Submitted Successfully!",Toast.LENGTH_LONG).show()
                                            val intent= Intent(this,DashboardActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }.addOnFailureListener {
                                            Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                            binding.progressBar.isVisible=false
                                            binding.submit.isClickable=true
                                            binding.submit.isVisible=true
                                        }
                            }.addOnFailureListener {
                                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                binding.progressBar.isVisible=false
                                binding.submit.isClickable=true
                                binding.submit.isVisible=true
                            }
                }
            }
        }
    }

    private fun checkForUser(email: String):Boolean {
        val query: Query?=db.collection("Users").document(email).collection("Account Info")
        var re=false;
        try {
            query!!.get().addOnCompleteListener {
                val r = it.result!!.isEmpty
                if (r) {
                    Toast.makeText(this,"No Such User Exists Yet!",Toast.LENGTH_LONG).show()
                    binding.submit.isVisible=true
                    binding.submit.isClickable=true
                    re=!r
                    progressBar.isVisible=false
                } else {
                    //Toast.makeText(this,"Proceed Safely",Toast.LENGTH_LONG).show()
                    re=!r
                }
            }
            return re
        }catch(e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            binding.submit.isVisible=true
            binding.submit.isClickable=true
            progressBar.isVisible=false
            return false
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