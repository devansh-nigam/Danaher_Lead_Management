package com.example.danaherleadmanagement

data class EnterpriseDomain(val image:Int, val name:String){
    object EnterpriseDomains{
        private val images= intArrayOf(R.drawable.danaherlogo,
            R.drawable.lab,
            R.drawable.earth,
            R.drawable.diagnostic)

        private val domains = arrayOf("All Segments",
            "Life Sciences",
            "Environmental & Applied Solutions",
            "Diagnostics")

        var list:ArrayList<EnterpriseDomain>?=null
            get(){
                if(field!=null)
                    return field

                field=ArrayList()
                for(i in images.indices){
                    val imageId=images[i]
                    val domainName= domains[i]

                    val domain=EnterpriseDomain(imageId,domainName)
                    field!!.add(domain)
                }
                return field
            }

    }
}
