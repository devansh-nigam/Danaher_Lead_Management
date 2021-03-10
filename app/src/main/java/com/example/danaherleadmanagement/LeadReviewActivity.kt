package com.example.danaherleadmanagement

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.danaherleadmanagement.databinding.ActivityLeadReviewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
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
        //binding.Email.text=SubmittedBy
        binding.cardViewPersonalLead.userEmail.text=SubmittedBy

        //db.collection("Users").document(SubmittedBy).collection("Account Ifno")


        binding.statusOfLead.text=status

        binding.validation.isVisible=false
        binding.rejection.isVisible=false
        binding.closure.isVisible=false

        if(status=="Open") binding.statusOfLead.setTextColor(ContextCompat.getColor(this, R.color.open_green))
        else if(status=="Validated") binding.statusOfLead.setTextColor(ContextCompat.getColor(this, R.color.orange))
        else if(status=="Rejected") binding.statusOfLead.setTextColor(ContextCompat.getColor(this, R.color.rejected_red))
        else if(status=="Closed") binding.statusOfLead.setTextColor(ContextCompat.getColor(this, R.color.yellow))

        binding.submittedOn.text=TimestampSubmission

        if(SubmittedBy.equals(user!!.email)) leadIs="Submitted Leads"
        else leadIs="Assigned Leads"

        if(leadIs=="Submitted Leads"){
            binding.impButtons.isVisible=false
            binding.close.isVisible=false
        }
        else if(leadIs=="Assigned Leads"){
            binding.close.isVisible=false
            if(status=="Validated"){
                binding.impButtons.isVisible=false
                binding.close.isVisible=true
            }
            else if(status=="Rejected"){
                binding.impButtons.isVisible=false
                binding.close.isVisible=false
            }
            else if(status=="Closed"){
                binding.impButtons.isVisible=false
                binding.close.isVisible=false
            }
        }

        db.collection("Users").document(user.email!!).collection(leadIs).document(hash.toString()).get()
            .addOnSuccessListener {
                 binding.opp.text=it.data!!.get("Opportunity") as String?
                 binding.valuee.text=it.data!!.get("Value") as String?
                 binding.seg.text=it.data!!.get("Segment") as String?
                val status=it.data!!.get("Status") as String?
                if(status=="Validated"){
                    binding.validation.isVisible=true
                    binding.validatedOn.text=it.data!!.get("TimestampValidated") as String?
                }else if(status=="Rejected"){
                    binding.rejection.isVisible=true
                    binding.rejectedOn.text=it.data!!.get("TimestampRejected") as String?
                }else if(status=="Closed"){
                    binding.closure.isVisible=true
                    binding.closedOn.text=it.data!!.get("TimestampClosed") as String?
                }

        }

        binding.validate.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to validate this Lead?")
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                        proceedWithLeadValidation(db,hash,SubmittedBy.toString())
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
                        proceedWithLeadRejection(db,hash,SubmittedBy.toString())
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

        binding.close.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to reject this Lead?")
                    // if the dialog is cancelable
                    .setCancelable(true)
                    // positive button text and action
                    .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                        proceedWithLeadClosure(db,hash,SubmittedBy.toString())
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                    })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Confirm Closure")
            // show alert dialog
            alert.show()
        }
    }

    private fun proceedWithLeadClosure(db: FirebaseFirestore, hash: String?, email: String) {
        val now=Timestamp.now()
        val leadDetails = hashMapOf(
                "Status" to "Closed",
                "TimestampClosed" to now.toDate().toString())

        val currentUser=mAuth.currentUser
        db.collection("Users").document(currentUser!!.email!!).collection("Assigned Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                .addOnSuccessListener {
                    db.collection("Users").document(email).collection("Submitted Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Lead Closed Successfully!",Toast.LENGTH_LONG).show()
                                val intent= Intent(this,DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                binding.close.isClickable=true
//                                progressBar.isVisible=false
                            }
                }.addOnFailureListener {
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
//                    progressBar.isVisible=false
                    binding.close.isClickable=true
                }
    }

    private fun proceedWithLeadValidation(db:FirebaseFirestore,hash:String?,email:String){

        val now=Timestamp.now()
        val leadDetails = hashMapOf(
                "Status" to "Validated",
                "TimestampValidated" to now.toDate().toString())

        val currentUser=mAuth.currentUser
        db.collection("Users").document(currentUser!!.email!!).collection("Assigned Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                .addOnSuccessListener {
                    db.collection("Users").document(email).collection("Submitted Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Lead Validated Successfully!",Toast.LENGTH_LONG).show()
                                val intent= Intent(this,DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                binding.validate.isClickable=true
//                                progressBar.isVisible=false
                            }
                }.addOnFailureListener {
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
//                    progressBar.isVisible=false
                    binding.validate.isClickable=true
                }

    }

    private fun proceedWithLeadRejection(db:FirebaseFirestore,hash:String?,email:String) {
        val now=Timestamp.now()
        val leadDetails = hashMapOf(
                "Status" to "Rejected",
                "TimestampRejected" to now.toDate().toString())

        val currentUser=mAuth.currentUser
        db.collection("Users").document(currentUser!!.email!!).collection("Assigned Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                .addOnSuccessListener {
                    db.collection("Users").document(email).collection("Submitted Leads").document(hash.toString()).update(leadDetails as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Lead Rejected Successfully!",Toast.LENGTH_LONG).show()
                                val intent= Intent(this,DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
                                binding.reject.isClickable=true
//                                progressBar.isVisible=false
                            }
                }.addOnFailureListener {
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
//                    progressBar.isVisible=false
                    binding.reject.isClickable=true
                }
    }
}