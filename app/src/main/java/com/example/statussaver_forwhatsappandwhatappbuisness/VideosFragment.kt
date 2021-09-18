package com.example.statussaver_forwhatsappandwhatappbuisness


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class VideosFragment : Fragment(R.layout.fragment_videos), VideoClicked {
    private lateinit var recyclerView:RecyclerView
    private lateinit var path:String
    private lateinit var adapter:VideoViewAdapter
    private lateinit var data:Data
    private var what = false
    private var pos = 0
    private var state = false
    private var flag = false
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val bool:Boolean = data?.getBooleanExtra("saved",false) == true
             if(bool){
                 tick()
              }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.videoRecycle)
        path = if(Build.VERSION.SDK_INT>=30) {
            if (Constants.businessCheck) {
                Constants.status_11B
            } else {
                Constants.status_11
            }
        } else {
            if (Constants.businessCheck) {
                Constants.statusPathB
            } else {
                Constants.statusPath
            }
        }
        adapter = VideoViewAdapter(view.context,this)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter = adapter
        data = Data()
        adapter.updateData(data.fetchData(".mp4",path))
        flag = true
    }

    override fun handleVideoClick(view: View, file: File,position:Int) {
        pos = position
        val intent = Intent(view.context,MediaViewActivity::class.java)
        intent.putExtra("Uri",file.absolutePath)
        intent.putExtra("type",false)
        intent.putExtra("name",file.name)
        intent.putExtra("pos",position)
        Constants.flag = false
        resultLauncher.launch(intent)
    }
    fun checkCreated():Boolean{
        return flag
    }
    fun notifyVideo(){
        state = true
    }
     fun update(){
        adapter.updateData(data.fetchData(".mp4",path))
    }
    private fun tick(){
        adapter.notifyItemChanged(pos)
    }
    private fun listUpdate(){
        Constants.videoRefresh.forEach {
            adapter.notifyItemChanged(it)
        }
        Constants.videoRefresh.clear()
    }
    override fun onResume() {
        super.onResume()
        if(what){

            whatsappChanged()
        }
        if(state || what){
            update()
            what = false
            state = false
        }
        else if(Constants.videoCheck){
            listUpdate()
            Constants.videoCheck = false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        flag = false
    }
    companion object {
        private var frag:VideosFragment? = null
        fun newInstance(): VideosFragment{
            if(frag==null){
                frag = VideosFragment()
            }
            return frag as VideosFragment
        }
    }
    fun whatChanged(){
        what = true
    }
    fun whatsappChanged(){
        path = if(Build.VERSION.SDK_INT>=30) {
            if (Constants.businessCheck) {
                Constants.status_11B
            } else {
                Constants.status_11
            }
        } else {
            if (Constants.businessCheck) {
                Constants.statusPathB
            } else {
                Constants.statusPath
            }
        }
    }
    override fun handleVideoSave(view: View, file: File,sender:Boolean,position: Int) {
        if(File(Constants.savePath+"/"+file.name).exists()){
            return
        }
        val status =  SaveStatus.copy(path,file.name,Constants.savePath,position,view.context)
        if(status){
            if(sender) {
                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(context,"Unable To Save File", Toast.LENGTH_SHORT).show()
        }
        Constants.list.add(File(Constants.savePath+"/"+file.name))
        Constants.savedCheck = true
    }
}