package com.example.statussaver_forwhatsappandwhatappbuisness


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import java.io.File

class ImagesFragment : Fragment(R.layout.fragment_images),ImageClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var data: Data
    private lateinit var path:String
    private lateinit var adapter:ImageRecyclerView
    private var what = false
    private var flag = false
    private var state = false
    private var pos = 0
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
             val data: Intent? = result.data
             val bool:Boolean = data?.getBooleanExtra("saved",false) == true
             if(bool){
                tick()
             }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            recyclerView = view.findViewById(R.id.imagesRecycle)
            path = if (Build.VERSION.SDK_INT >= 30) {
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
            adapter = ImageRecyclerView(view.context, this)
            recyclerView.layoutManager = GridLayoutManager(context, 3)
            recyclerView.adapter = adapter
            data = Data()
            update()
            flag = true
        }catch (e:Exception){
            Toast.makeText(context,"Unknown Error Occurred",Toast.LENGTH_SHORT).show()
        }
    }
    fun checkCreated(): Boolean {
        return flag
    }
    fun notifyImage(){
        state=true
    }
    fun whatChanged()
    {
        what = true
    }
    private fun whatsappChanged(){
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
    fun update(){
        adapter.updateData(data.fetchData(".jpg",path))
    }
    private fun tick(){
        adapter.notifyItemChanged(pos)
    }
    private fun listUpdate(){
        Constants.imageRefresh.forEach {
            adapter.notifyItemChanged(it)
        }
        Constants.imageRefresh.clear()
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
        else if(Constants.imageCheck){
            listUpdate()
            Constants.imageCheck = false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        flag = false
    }
    companion object {
        private var frag:ImagesFragment? = null
        fun newInstance(): ImagesFragment {
            if(frag==null){
                frag = ImagesFragment()
            }
            return frag as ImagesFragment
        }
    }

    override fun handleImageClick(view: View,file: File,position:Int) {
        try {
            val intent = Intent(view.context, MediaViewActivity::class.java)
            pos = position
            intent.putExtra("Uri", file.absolutePath)
            intent.putExtra("name", file.name)
            intent.putExtra("type", true)
            intent.putExtra("pos", position)
            Constants.flag = false
            resultLauncher.launch(intent)
        }catch (e:Exception){

        }
    }

    override fun handleSave(view: View, file: File,sender:Boolean,position: Int) {
        try {
            if (File(Constants.savePath + "/" + file.name).exists()) {
                return
            }
            val status =
                SaveStatus.copy(path, file.name, Constants.savePath, position, view.context)
            if (status) {
                if (sender) {
                    Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Unable To Save File", Toast.LENGTH_SHORT).show()
            }
            Constants.list.add(File(Constants.savePath + "/" + file.name))
            Constants.savedCheck = true
        }catch (e:Exception){
            Toast.makeText(context,"Unable To Save File",Toast.LENGTH_SHORT).show()
        }
    }
}