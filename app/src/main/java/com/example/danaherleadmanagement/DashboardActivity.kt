package com.example.danaherleadmanagement

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.danaherleadmanagement.databinding.ActivityDashboardBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mAuth:FirebaseAuth;
    private var layoutManager: RecyclerView.LayoutManager?=null
    var assignedLeadAdapter:LeadAdapter?=null
    var submittedLeadAdapter:LeadAdapter?=null

    val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mAuth = FirebaseAuth.getInstance()
        val user=mAuth.currentUser;

        if(user==null){
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        db.firestoreSettings = settings

        binding.noAssignedImg.isVisible=false
        binding.noAssignedTxt.isVisible=false

        binding.noSubmittedImg.isVisible=false
        binding.noSubmittedTxt.isVisible=false


        checkForNoAssignedLeads(db)
        setUpRecyclerViewForAssignedLeads()
        checkForNoSubmittedLeads(db)
        setUpRecyclerViewForSubmittedLeads()

        var full_name=""
        var domain=""
        var company=""

            db.collection("Users").document(user.email!!).collection("Account Info").get().addOnSuccessListener {
                full_name=it.documents[0]["Name"] as String
                domain=it.documents[0]["Domain"] as String
                company=it.documents[0]["OpCo"] as String

                binding.cardViewPersonal.userName.text=full_name

                binding.cardViewPersonal.textDomain.text=domain
                if(domain=="Life Sciences"){
                    binding.cardViewPersonal.imageDomain.setImageResource(R.drawable.lab)
                }else if(domain=="Environmental & Applied Solutions"){
                    binding.cardViewPersonal.imageDomain.setImageResource(R.drawable.earth)
                }
                else if(domain=="Diagnostics"){
                    binding.cardViewPersonal.imageDomain.setImageResource(R.drawable.diagnostic)
                }
                binding.cardViewPersonal.textCompany.text=company
        }
        binding.cardViewPersonal.userEmail.text=user.email


        binding.viewAllAssigned.setOnClickListener {
            val intent=Intent(this,AssignedLeadsDashboardActivity::class.java)
            intent.putExtra("Leads","Assigned Leads")
            startActivity(intent)
        }

        binding.viewAllSubmitted.setOnClickListener {
            val intent=Intent(this,AssignedLeadsDashboardActivity::class.java)
            intent.putExtra("Leads","Submitted Leads")
            startActivity(intent)
        }

        binding.submitLead.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }

            val intent=Intent(this,LeadSubmissionActivity::class.java)
            intent.putExtra("Name",full_name)
            intent.putExtra("Domain",domain)
            intent.putExtra("OpCo",company)
            startActivity(intent)
        }
    }

    private fun checkForNoAssignedLeads(db:FirebaseFirestore) {
        val query: Query?=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
        try {
            query!!.get().addOnCompleteListener {
               val r = it.result!!.isEmpty
                if (r) {
                    binding.noAssignedImg.isVisible = true
                    binding.noAssignedTxt.isVisible = true
                    binding.viewAllAssigned.isVisible=false
                } else {
                    binding.noAssignedImg.isVisible = false
                    binding.noAssignedTxt.isVisible = false
                    binding.viewAllAssigned.isVisible=true
                }
            }
        }catch(e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun checkForNoSubmittedLeads(db:FirebaseFirestore) {
        val query: Query?=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Submitted Leads")
        try {
            query!!.get().addOnCompleteListener {
                val r = it.result!!.isEmpty
                if (r) {
                    binding.noSubmittedImg.isVisible = true
                    binding.noSubmittedTxt.isVisible = true
                    binding.viewAllSubmitted.isVisible=false
                } else {
                    binding.noSubmittedImg.isVisible = false
                    binding.noSubmittedTxt.isVisible = false
                    binding.viewAllSubmitted.isVisible=true
                }
            }
        }catch(e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpRecyclerViewForSubmittedLeads() {
        val query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Submitted Leads")//.orderBy("TimestampSubmission")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<LeadModel> = FirestoreRecyclerOptions.Builder<LeadModel>().
        setQuery(query,LeadModel::class.java).build()

        submittedLeadAdapter= LeadAdapter(firestoreRecyclerOptions)
        layoutManager= LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerViewSubmittedLeads.layoutManager=layoutManager
        binding.recyclerViewSubmittedLeads.adapter=submittedLeadAdapter
    }

    private fun setUpRecyclerViewForAssignedLeads() {
        val query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<LeadModel> = FirestoreRecyclerOptions.Builder<LeadModel>().
        setQuery(query,LeadModel::class.java).build()

        assignedLeadAdapter= LeadAdapter(firestoreRecyclerOptions)
        layoutManager= LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerViewAssignedLeads.layoutManager=layoutManager
        binding.recyclerViewAssignedLeads.adapter=assignedLeadAdapter
    }

    //Menu Stuff
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOut -> {
                signOut(mAuth)
            }
            R.id.contact->{
                    val intent=Intent(this,WebActivity::class.java)
                    intent.putExtra("Title","Contact")
                    startActivity(intent)
            }
            R.id.terms->{
                    val intent=Intent(this,WebActivity::class.java)
                    intent.putExtra("Title","Terms")
                    startActivity(intent)
            }
            R.id.privacyPolicy->{
                    val intent=Intent(this,WebActivity::class.java)
                    intent.putExtra("Title","Privacy")
                    startActivity(intent)
                }
        }
        return super.onOptionsItemSelected(item)
    }

    //Signing out!
    private fun signOut(mAuth:FirebaseAuth)
    {
        mAuth.signOut()
        val intent=Intent(this@DashboardActivity,LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        checkForNoAssignedLeads(db)
        assignedLeadAdapter!!.startListening()
        checkForNoSubmittedLeads(db)
        submittedLeadAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        assignedLeadAdapter!!.stopListening()
        submittedLeadAdapter!!.stopListening()
    }
}