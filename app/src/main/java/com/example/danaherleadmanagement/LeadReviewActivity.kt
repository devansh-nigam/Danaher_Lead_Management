package com.example.danaherleadmanagement

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.danaherleadmanagement.databinding.ActivityLeadReviewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class LeadReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeadReviewBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLeadReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser

        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

        var leadIs=""

        val Name=intent.getStringExtra("Name")
        val SubmittedBy=intent.getStringExtra("SubmittedBy")
        val TimestampSubmission=intent.getStringExtra("TimestampSubmission")
        val status=intent.getStringExtra("Status")
        val hash=intent.getStringExtra("Hash")

        binding.cusName.text=Name
        binding.Email.text=SubmittedBy
        binding.statusOfLead.text=status
        binding.submittedOn.text=TimestampSubmission

        if(SubmittedBy.equals(user!!.email)) leadIs="Submitted Leads"
        else leadIs="Assigned Leads"

        if(leadIs=="Submitted Leads"){
            binding.impButtons.isVisible=false
        }

        db.collection("Users").document(user.email!!).collection(leadIs).document(hash.toString()).get()
            .addOnSuccessListener {
                 binding.opp.text=it.data!!.get("Opportunity") as String?
                 binding.valuee.text=it.data!!.get("Value") as String?
                 binding.seg.text=it.data!!.get("Segment") as String?
        }

        binding.validate.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to validate this Lead?")
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                    {
                        proceedWithLeadValidation()
                    }
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Confirm Validation")
            // show alert dialog
            alert.show()
        }


        binding.reject.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to reject this Lead?")
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                    {
                        proceedWithLeadRejection()
                    }
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Confirm Rejection")
            // show alert dialog
            alert.show()
        }
        }

    private fun proceedWithLeadValidation(){

    }

    private fun proceedWithLeadRejection() {

    }
}