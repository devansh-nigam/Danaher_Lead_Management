package com.example.danaherleadmanagement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recycler_view_model.view.*

class LeadAdapter(options: FirestoreRecyclerOptions<LeadModel>) :
    FirestoreRecyclerAdapter<LeadModel, LeadAdapter.LeadAdapterVH>(options)
{
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadAdapterVH {
        mContext=parent.context
        return LeadAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_model,parent,false))
    }

    override fun onBindViewHolder(holder: LeadAdapterVH, position: Int, model: LeadModel) {
        holder.leadName.text=model.Name
        holder.leadEmail.text=model.SubmittedBy
        holder.leadTimestamp.text=model.TimestampSubmission
        holder.leadStatus.text=model.Status

        holder.itemView.setOnClickListener {
            val positionH=holder.adapterPosition
            val intent= Intent(mContext,LeadReviewActivity::class.java)
            val l=getItem(positionH)
            intent.putExtra("Name",l.Name)
            intent.putExtra("TimestampSubmission",l.TimestampSubmission)
            intent.putExtra("SubmittedBy",l.SubmittedBy)
            intent.putExtra("Status",l.Status)
            intent.putExtra("Hash",l.Hash)
            mContext.startActivity(intent)
        }
    }

    class LeadAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leadName=itemView.user_name
        var leadEmail=itemView.user_email
        var leadTimestamp=itemView.timestampSubmission
        var leadStatus=itemView.status
    }
}