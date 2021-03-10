package com.example.danaherleadmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.danaherleadmanagement.databinding.ActivityAssignedLeadsDashboardBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_assigned_leads_dashboard.*

class AssignedLeadsDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssignedLeadsDashboardBinding
    private lateinit var mAuth: FirebaseAuth;
    private var layoutManager: RecyclerView.LayoutManager?=null
    var assignedLeadAdapter:LeadAdapter?=null

    val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }
    //private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAssignedLeadsDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()


        val query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<LeadModel> = FirestoreRecyclerOptions.Builder<LeadModel>().
        setQuery(query,LeadModel::class.java).build()
        assignedLeadAdapter= LeadAdapter(firestoreRecyclerOptions)
        layoutManager= LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        binding.recyclerViewAssignedLeads.layoutManager=layoutManager
        binding.recyclerViewAssignedLeads.adapter=assignedLeadAdapter

        binding.openCheck.setOnClickListener {
            checkCheckBoxes()
        }
        binding.closedCheck.setOnClickListener {
            checkCheckBoxes()
        }
        binding.validateCheck.setOnClickListener {
            checkCheckBoxes()
        }
        binding.rejectedCheck.setOnClickListener {
            checkCheckBoxes()
        }
        checkCheckBoxes()
        //setUpRecyclerViewForAssignedLeads()
    }

    private fun checkCheckBoxes()
    {
        var query:Query
        var Open=binding.openCheck.isChecked

        var Validated=binding.validateCheck.isChecked
        var Closed=binding.closedCheck.isChecked
        var Rejected=binding.rejectedCheck.isChecked
        
//        if(Open && Validated && Closed && Rejected){
//            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
//            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
//        }
        if(Open && Validated && Closed && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereNotEqualTo("Status","Rejected")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(Open && Validated && Rejected && !Closed)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereNotEqualTo("Status","Closed")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(Open && Closed && Rejected && !Validated)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereNotEqualTo("Status","Validated")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(Validated && Closed && Rejected && !Open)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereNotEqualTo("Status","Open")
        }
        //2 at a time
        else if(Open && Validated && !Closed && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Open","Validated"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }

        else if(Open && Closed && !Validated && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Open","Closed"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(Open && Rejected && !Validated && !Closed)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Open","Rejected"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }

        else if(Validated && Closed && !Open && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Validated","Closed"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }

        else if(Validated && Rejected && !Open && !Closed)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Validated","Rejected"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }

        else if(Closed && Rejected && !Open && !Validated)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereIn("Status", arrayListOf("Rejected","Closed"))
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        //1 at a time
        else if(Open && !Validated && !Closed && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereEqualTo("Status","Open")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(!Open && Validated && !Closed && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereEqualTo("Status","Validated")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(!Open && !Validated && Closed && !Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereEqualTo("Status","Closed")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else if(!Open && !Validated && !Closed && Rejected)
        {
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
                .whereEqualTo("Status","Rejected")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }
        else{
            query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection("Assigned Leads")
            //Toast.makeText(this,"Open=$Open\nValidated=$Validated\nClosed=$Closed\nRejected=$Rejected",Toast.LENGTH_LONG).show()
        }

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<LeadModel> = FirestoreRecyclerOptions.Builder<LeadModel>().
        setQuery(query,LeadModel::class.java).build()

        assignedLeadAdapter= LeadAdapter(firestoreRecyclerOptions)
        layoutManager= LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        binding.recyclerViewAssignedLeads.layoutManager=layoutManager
        binding.recyclerViewAssignedLeads.adapter=assignedLeadAdapter

    }


    override fun onStart() {
        super.onStart()
    binding.openCheck.setOnClickListener {
        checkCheckBoxes()
        assignedLeadAdapter!!.startListening()
    }
    binding.closedCheck.setOnClickListener {
        checkCheckBoxes()
        assignedLeadAdapter!!.startListening()
    }
    binding.validateCheck.setOnClickListener {
        checkCheckBoxes()
        assignedLeadAdapter!!.startListening()
    }
    binding.rejectedCheck.setOnClickListener {
        checkCheckBoxes()
        assignedLeadAdapter!!.startListening()

    }
    checkCheckBoxes()
    Toast.makeText(this,"onStart()",Toast.LENGTH_LONG).show()
        assignedLeadAdapter!!.startListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"onDestroy()",Toast.LENGTH_LONG).show()
        assignedLeadAdapter!!.stopListening()
    }
}