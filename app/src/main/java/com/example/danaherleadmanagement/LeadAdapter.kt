package com.example.danaherleadmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recycler_view_model.view.*

class LeadAdapter(options: FirestoreRecyclerOptions<LeadModel>) :
    FirestoreRecyclerAdapter<LeadModel, LeadAdapter.LeadAdapterVH>(options)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadAdapterVH {
        return LeadAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model,parent,false))
    }

    override fun onBindViewHolder(holder: LeadAdapterVH, position: Int, model: LeadModel) {
        holder.leadName.text=model.Name
        holder.leadEmail.text=model.SubmittedBy
        holder.leadTimestamp.text=model.TimestampSubmission
        holder.leadStatus.text=model.Status
    }

    class LeadAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leadName=itemView.user_name
        var leadEmail=itemView.user_email
        var leadTimestamp=itemView.timestampSubmission
        var leadStatus=itemView.status
    }
}