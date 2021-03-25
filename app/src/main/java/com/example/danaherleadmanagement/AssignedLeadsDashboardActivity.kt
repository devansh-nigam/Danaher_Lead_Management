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
    var leads:String?=null
    val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAssignedLeadsDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        supportActionBar!!.hide()

        leads=intent.getStringExtra("Leads")
        if(leads=="Submitted Leads"){
            binding.las.text="Submitted Leads"
        }

        val query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection(leads.toString()).orderBy("TimestampLatest",Query.Direction.DESCENDING)
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
    }

    private fun checkCheckBoxes()
    {
        var query:Query
        var Open=binding.openCheck.isChecked

        var Validated=binding.validateCheck.isChecked
        var Closed=binding.closedCheck.isChecked
        var Rejected=binding.rejectedCheck.isChecked
        var q:Query
        query=db.collection("Users").document(mAuth.currentUser!!.email!!).collection(leads.toString())//.orderBy("TimestampSubmission",Query.Direction.DESCENDING)
        if(Open && Validated && Closed && !Rejected)
        {
               q=query.whereNotEqualTo("Status","Rejected")
        }
        else if(Open && Validated && Rejected && !Closed)
        {
              q=  query.whereNotEqualTo("Status","Closed")
        }
        else if(Open && Closed && Rejected && !Validated)
        {
            q=query.whereNotEqualTo("Status","Validated")
        }
        else if(Validated && Closed && Rejected && !Open)
        {
            q=query.whereNotEqualTo("Status","Open")
        }
        //2 at a time
        else if(Open && Validated && !Closed && !Rejected)
        {
            q=query.whereIn("Status", arrayListOf("Open","Validated"))
        }

        else if(Open && Closed && !Validated && !Rejected)
        {
            q=query.whereIn("Status", arrayListOf("Open","Closed"))
        }
        else if(Open && Rejected && !Validated && !Closed)
        {
            q=query.whereIn("Status", arrayListOf("Open","Rejected"))
        }

        else if(Validated && Closed && !Open && !Rejected)
        {
            q=query.whereIn("Status", arrayListOf("Validated","Closed"))
        }

        else if(Validated && Rejected && !Open && !Closed)
        {
            q=query.whereIn("Status", arrayListOf("Validated","Rejected"))
        }

        else if(Closed && Rejected && !Open && !Validated)
        {
            q=query.whereIn("Status", arrayListOf("Rejected","Closed"))
        }
        //1 at a time
        else if(Open && !Validated && !Closed && !Rejected)
        {
            q=query.whereEqualTo("Status","Open")
        }
        else if(!Open && Validated && !Closed && !Rejected)
        {
            q=query.whereEqualTo("Status","Validated")
        }
        else if(!Open && !Validated && Closed && !Rejected)
        {
            q=query.whereEqualTo("Status","Closed")
        }
        else if(!Open && !Validated && !Closed && Rejected)
        {
            q=query.whereEqualTo("Status","Rejected")
        }
        else{
            q=db.collection("Users").document(mAuth.currentUser!!.email!!).collection(leads.toString()).orderBy("TimestampSubmission",Query.Direction.DESCENDING)
        }

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<LeadModel> = FirestoreRecyclerOptions.Builder<LeadModel>().
        setQuery(q,LeadModel::class.java).build()

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
        assignedLeadAdapter!!.startListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        assignedLeadAdapter!!.stopListening()
    }
}