package com.example.danaherleadmanagement

data class LifeScience(val image:Int, val name:String){
    object LifeSciences{
        private val images= intArrayOf(R.drawable.danaherlogo,
            R.drawable.beckman,
            R.drawable.cytiva,
            R.drawable.idt,
            R.drawable.leica,
            R.drawable.molecular,
            R.drawable.pall,
            R.drawable.phenomenex,
            R.drawable.sciex)

        private val companies = arrayOf("Life Sciences OpCo",
            "Beckman Coulter Life Sciences",
            "Cytiva",
            "Integrated DNA Technologies (IDT)",
            "Leica Microsystems",
            "Molecular Devices",
            "Pall",
            "Phenomenex",
            "SCIEX")

        var list:ArrayList<LifeScience>?=null
            get(){
                if(field!=null)
                    return field

                field=ArrayList()
                for(i in images.indices){
                    val imageId=images[i]
                    val companyName= companies[i]

                    val company=LifeScience(imageId,companyName)
                    field!!.add(company)
                }
                return field
            }

    }
}
