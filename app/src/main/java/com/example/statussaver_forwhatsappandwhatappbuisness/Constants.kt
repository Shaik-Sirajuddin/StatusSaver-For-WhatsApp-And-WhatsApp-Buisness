package com.example.statussaver_forwhatsappandwhatappbuisness

import android.os.Environment
import java.io.File

class Constants {
    companion object{
        var flag = true
        var savedCheck = false
        var videoCheck = false
        var imageCheck = false
        val mail = "techx2002@gmail.com"
        var map = hashMapOf<String,Int>()
        var videoRefresh = ArrayList<Int>()
        var imageRefresh = ArrayList<Int>()
        var list = ArrayList<File>()
        var autoRefresh = true
        var businessCheck = false
        private val externalDir:String = Environment.getExternalStorageDirectory().absolutePath
        val refreshCheck = "$externalDir/SavedStatus/Settings/.AutoRefresh"
        val business = "$externalDir/SavedStatus/Settings/.Business"
        val status_11:String = "$externalDir/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        val statusPath:String = "$externalDir/WhatsApp/Media/.Statuses"
        val savePath = "$externalDir/SavedStatus"
        val status_11B:String =
            "$externalDir/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
        val statusPathB:String = "$externalDir/WhatsApp Business/Media/.Statuses"
    }
}