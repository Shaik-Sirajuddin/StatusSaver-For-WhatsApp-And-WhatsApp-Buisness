package com.example.statussaver_forwhatsappandwhatappbuisness


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.provider.MediaStore
import android.media.ThumbnailUtils
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VideoViewAdapter(val context:Context, private val listner:VideoClicked): RecyclerView.Adapter<VideoHolder>() {

    private val list = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val layout  = LayoutInflater.from(context).inflate(R.layout.video_item,parent,false)
        val holder =  VideoHolder(layout)
         layout.setOnClickListener {
             listner.handleVideoClick(it,list[holder.absoluteAdapterPosition],holder.absoluteAdapterPosition)
         }
        holder.saveV.setOnClickListener {
            holder.saveV.setImageResource(R.drawable.done)
            listner.handleVideoSave(it,list[holder.absoluteAdapterPosition],true,holder.absoluteAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        try {
            Constants.map[list[holder.absoluteAdapterPosition].name.toString()] =
                holder.absoluteAdapterPosition


            if (File(Constants.savePath + "/" + list[position].name).exists()) {
                holder.saveV.setImageResource(R.drawable.done)
            } else {
                holder.saveV.setImageResource(R.drawable.save_img)
            }
            if (!list[position].exists()) {
                list.removeAt(position)
                notifyItemRemoved(position)
                return
            }
            GlobalScope.launch(Dispatchers.IO) {
                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                    list[position].absolutePath,
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
                )
                withContext(Dispatchers.Main) {
                    Glide.with(context).load(thumbnail).into(holder.videoPlace)
                }
            }
        }catch (e:Exception){
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateData(data:ArrayList<File>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}

class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val videoPlace: ImageView = itemView.findViewById(R.id.video_view)
    val saveV: ImageView = itemView.findViewById(R.id.saveV)

}
interface VideoClicked{
    fun handleVideoClick(view:View,file:File,position: Int)
    fun handleVideoSave(view:View,file:File,sender:Boolean,position: Int)

}