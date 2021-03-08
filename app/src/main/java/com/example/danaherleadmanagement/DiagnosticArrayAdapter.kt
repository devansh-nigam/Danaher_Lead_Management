package com.example.danaherleadmanagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*

class DiagnosticArrayAdapter(context: Context,companyList:List<Diagnostic>):ArrayAdapter<Diagnostic>(context,0,companyList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup):View{
        val company=getItem(position)
        val view=LayoutInflater.from(context).inflate(R.layout.spinner_item,parent,false)
        view.image_domain.setImageResource(company!!.image)
        view.text_domain.text=company.name
        return view
    }
}