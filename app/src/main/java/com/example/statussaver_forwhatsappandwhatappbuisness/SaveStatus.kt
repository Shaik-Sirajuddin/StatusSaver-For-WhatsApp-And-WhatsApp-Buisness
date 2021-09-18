package com.example.statussaver_forwhatsappandwhatappbuisness


import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.Exception


class SaveStatus {
    companion object {
        fun updateAutoRefresh(){
            Constants.autoRefresh = isAutoRefreshEnabled()
        }
        fun isAutoRefreshEnabled():Boolean{
            if(File(Constants.refreshCheck).exists())return false
            return true
        }
        fun copy(inputPath: String, inputFile: String, outputPath: String,position:Int,context: Context): Boolean {
            var flag = true
            GlobalScope.launch(Dispatchers.IO){
               flag =  copyFile("$inputPath/",inputFile,outputPath,context)
            }
            return flag
        }
        fun copyFile(inputPath: String, inputFile: String, outputPath: String,context:Context): Boolean {

                var flag = true
                var input: InputStream?
                var out: OutputStream?
                try {

                    val dir = File(outputPath)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }

                    input = FileInputStream(inputPath + inputFile)
                    out = FileOutputStream("$outputPath/$inputFile")

                        val buffer = ByteArray(1024)
                        var read: Int
                        while (input.read(buffer).also { read = it } !== -1) {
                            out.write(buffer, 0, read)
                        }
                        input.close()
                        input = null
                        // write the output file (You have now copied the file)
                        out.flush()
                        out.close()
                        out = null
                    MediaScannerConnection.scanFile(context, arrayOf(dir.absolutePath),null){ s, uri ->

                    }

                } catch (fnfe1: FileNotFoundException) {
                    flag = false
                    Log.e("tag", fnfe1.message.toString())
                } catch (e: Exception) {
                    flag = false
                    Log.e("tag", e.message.toString())
                }

                return flag

        }
    }

}