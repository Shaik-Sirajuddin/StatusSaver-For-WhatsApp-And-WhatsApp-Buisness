package com.example.statussaver_forwhatsappandwhatappbuisness

import java.io.File

class Data {

    fun fetchData(end:String,path:String): ArrayList<File> {
            val listPath = File(path)
            val returnList = ArrayList<File>()
            if (!listPath.exists()) {
                return returnList
            }
            val list = listPath.listFiles()
            list?.forEach { item ->
                if (item.name.endsWith(end)) {
                    returnList.add(item)
                }
            }
            return returnList

    }

}