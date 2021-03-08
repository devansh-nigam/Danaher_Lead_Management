package com.example.danaherleadmanagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*

class DomainArrayAdapter(context: Context,domainList:List<EnterpriseDomain>):ArrayAdapter<EnterpriseDomain>(context,0,domainList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup):View{
        val domain=getItem(position)
        val view=LayoutInflater.from(context).inflate(R.layout.spinner_item,parent,false)
        view.image_domain.setImageResource(domain!!.image)
        view.text_domain.text=domain.name
        return view
    }
}