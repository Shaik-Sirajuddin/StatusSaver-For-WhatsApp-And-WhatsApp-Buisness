package com.example.statussaver_forwhatsappandwhatappbuisness

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class SavedFragment : Fragment(R.layout.fragment_saved), SavedItemClick {
     private lateinit var recyclerView:RecyclerView
     private lateinit var adapter:SavedMediaAdapter
     private var toast: Toast? = null
     private var res = false
     private var flag = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.savedRecycle)
        adapter = SavedMediaAdapter(view.context,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context,3)
        adapter.updateData(gData())
        flag = true
    }
    companion object {
        private var frag:SavedFragment? = null
        fun newInstance(): SavedFragment{
            if(frag==null){
                frag = SavedFragment()
            }
            return frag as SavedFragment
        }
    }

    override fun handleSavedItemClick(view: View, file: File) {
        val intent = Intent(view.context,MediaViewActivity::class.java)
        intent.putExtra("Uri",file.absolutePath)
        var fl = true
        if(file.name.endsWith(".mp4")){
            fl = false
        }
        intent.putExtra("type", fl)
        intent.putExtra("name",file.name)
        Constants.flag = false
        startActivity(intent)
    }
    private fun gData(): ArrayList<File> {
        val list =File(Constants.savePath).listFiles()
        val array = ArrayList<File>()
        list?.forEach {
             if(!it.isDirectory) {
                 array.add(it)
             }
        }
        return array
    }
    fun update(){
        adapter.updateData(gData())
    }
    fun checkCreated(): Boolean {
        return flag
    }
    fun notifySaved(){
        res = true
    }
    override fun handleDeleteClick(view: View, file: File) {
        file.delete()
        if(file.name.endsWith(".mp4")){
            Constants.videoCheck = true
            val inte = Constants.map[file.name]
            inte?.let { Constants.videoRefresh.add(it) }
        }
        else{
            Constants.imageCheck = true
            val inte = Constants.map[file.name]
            inte?.let { Constants.imageRefresh.add(it) }
        }
        toast?.cancel()
        toast = Toast.makeText(view.context,"Deleted",Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun onResume() {
        super.onResume()
        if(res){
             adapter.updateData(gData())
             res = false
        }
        else if(Constants.savedCheck){
            adapter.addData(Constants.list)
            Constants.list.clear()
            Constants.savedCheck = false
        }
    }
}