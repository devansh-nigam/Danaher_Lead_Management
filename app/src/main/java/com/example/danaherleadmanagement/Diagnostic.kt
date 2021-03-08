package com.example.danaherleadmanagement

data class Diagnostic(val image:Int, val name:String){
    object Diagnostics{
        private val images= intArrayOf(R.drawable.danaherlogo,
            R.drawable.beckman,
            R.drawable.cepheid,
            R.drawable.hemocue,
            R.drawable.leica,
            R.drawable.radiometer)

        private val companies = arrayOf("Diagnostics OpCo",
            "Beckman Coulter",
            "Cepheid",
            "HemoCue",
            "Leica Biosystems",
            "Radiometer")

        var list:ArrayList<Diagnostic>?=null
            get(){
                if(field!=null)
                    return field

                field=ArrayList()
                for(i in images.indices){
                    val imageId=images[i]
                    val companyName= companies[i]

                    val company=Diagnostic(imageId,companyName)
                    field!!.add(company)
                }
                return field
            }

    }
}
