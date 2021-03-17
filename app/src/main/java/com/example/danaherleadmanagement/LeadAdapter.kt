package com.example.danaherleadmanagement

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.common.io.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.recycler_view_model.view.*

class LeadAdapter(options: FirestoreRecyclerOptions<LeadModel>) :
    FirestoreRecyclerAdapter<LeadModel, LeadAdapter.LeadAdapterVH>(options)
{
    private lateinit var mContext: Context
    private lateinit var mAuth: FirebaseAuth;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadAdapterVH {
        mContext=parent.context
        mAuth = FirebaseAuth.getInstance()
        return LeadAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model,parent,false))
    }

    override fun onBindViewHolder(holder: LeadAdapterVH, position: Int, model: LeadModel) {
        holder.leadName.text=model.Name
        if(mAuth.currentUser.email==model.SubmittedBy){
            holder.leadAction.text="Submitted To:"
            holder.leadEmail.text=model.SubmittedTo
        }else holder.leadEmail.text=model.SubmittedBy
        //holder.leadTimestamp.text=model.TimestampSubmission
        holder.leadStatus.text=model.Status

        if(model.Status=="Open") {
            holder.leadStatus.setTextColor(getColor(mContext, R.color.open_green))
            holder.leadTimestamp.text=model.TimestampSubmission
        }
        else if(model.Status=="Validated") {
            holder.leadStatus.setTextColor(getColor(mContext,R.color.orange))
            holder.leadTimestamp.text=model.TimestampValidated
        }
        else if(model.Status=="Rejected"){
            holder.leadStatus.setTextColor(getColor(mContext,R.color.rejected_red))
            holder.leadTimestamp.text=model.TimestampRejected
        }
        else if(model.Status=="Closed"){
            holder.leadStatus.setTextColor(getColor(mContext,R.color.yellow))
            holder.leadTimestamp.text=model.TimestampClosed
        }

        holder.itemView.setOnClickListener {
            val positionH=holder.adapterPosition
            val intent= Intent(mContext,LeadReviewActivity::class.java)
            val l=getItem(positionH)
            intent.putExtra("Name",l.Name)
            intent.putExtra("TimestampSubmission",l.TimestampSubmission)
            intent.putExtra("SubmittedBy",l.SubmittedBy)
            intent.putExtra("SubmittedTo",l.SubmittedTo)
            intent.putExtra("Status",l.Status)
            intent.putExtra("Hash",l.Hash)
            mContext.startActivity(intent)
        }
    }

    class LeadAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leadName=itemView.user_name
        var leadEmail=itemView.user_email
        var leadAction=itemView.user_action
        var leadTimestamp=itemView.timestampSubmission
        var leadStatus=itemView.status
    }
}