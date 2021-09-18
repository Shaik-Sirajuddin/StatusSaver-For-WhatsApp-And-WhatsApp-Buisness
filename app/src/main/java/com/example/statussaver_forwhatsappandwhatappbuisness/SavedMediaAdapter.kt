package com.example.statussaver_forwhatsappandwhatappbuisness

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SavedMediaAdapter(val context: Context, private val listner:SavedItemClick):RecyclerView.Adapter<SavedViewHolder>() {

   private val list = ArrayList<File>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.saved_files,parent,false)
        val holder = SavedViewHolder(view)
        view.setOnClickListener {
            listner.handleSavedItemClick(it,list[holder.absoluteAdapterPosition])
        }
        holder.deleteImageView.setOnClickListener {
            listner.handleDeleteClick(it,list[holder.absoluteAdapterPosition])
            if(list[holder.absoluteAdapterPosition].name.endsWith(".mp4")){
                holder.playButton.visibility = View.GONE
            }
            list.removeAt(holder.absoluteAdapterPosition)
            notifyItemRemoved(holder.absoluteAdapterPosition)
            notifyItemRangeChanged(holder.absoluteAdapterPosition,list.size)
        }
        return holder
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
      try {
          if (list[position].name.endsWith(".mp4")) {
              GlobalScope.launch(Dispatchers.IO) {
                  val thumbnail = ThumbnailUtils.createVideoThumbnail(
                      list[position].absolutePath,
                      MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
                  )
                  withContext(Dispatchers.Main) {
                      Glide.with(context).load(thumbnail).into(holder.saveImgView)
                      holder.playButton.visibility = View.VISIBLE
                  }
              }
          } else {
              GlobalScope.launch(Dispatchers.IO) {
                  withContext(Dispatchers.Main) {
                      Glide.with(context).load(list[position]).into(holder.saveImgView)
                  }
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
    fun addData(data:ArrayList<File>){
        list.addAll(data)
        notifyItemRangeChanged(list.size-data.size,data.size)
    }
}

class SavedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val saveImgView: ImageView = itemView.findViewById(R.id.savedImage)
    val deleteImageView: ImageView = itemView.findViewById(R.id.deleteFile)
    val playButton: ImageView = itemView.findViewById(R.id.pBut)
}
interface SavedItemClick{
    fun handleSavedItemClick(view:View,file:File)
    fun handleDeleteClick(view:View,file:File)
}