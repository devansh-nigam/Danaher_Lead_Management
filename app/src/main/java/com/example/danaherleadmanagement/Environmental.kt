package com.example.danaherleadmanagement

data class Environmental(val image:Int, val name:String){
    object Environmentals{
        private val images= intArrayOf(R.drawable.danaherlogo,
            R.drawable.foba,
            R.drawable.chemtreat,
            R.drawable.esko,
            R.drawable.hach,
            R.drawable.laetus,
            R.drawable.linx,
            R.drawable.mccrometer,
            R.drawable.pantone,
            R.drawable.trojan,
            R.drawable.videojet,
            R.drawable.xrite)

        private val companies = arrayOf("Environmental OpCo",
            "ALLTEC/FOBA",
            "ChemTreat",
            "Esko",
            "Hach",
            "Laetus GmbH",
            "LINX",
            "McCrometer",
            "Pantone",
            "Trojan Technologies",
            "Videojet",
            "X-rite")

        var list:ArrayList<Environmental>?=null
            get(){
                if(field!=null)
                    return field

                field=ArrayList()
                for(i in images.indices){
                    val imageId=images[i]
                    val companyName= companies[i]

                    val company=Environmental(imageId,companyName)
                    field!!.add(company)
                }
                return field
            }

    }
}
