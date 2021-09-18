package com.example.statussaver_forwhatsappandwhatappbuisness

import android.content.Context
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


class ImageRecyclerView(val context:Context, private val listner:ImageClicked): RecyclerView.Adapter<ViewHolder>() {

    private val list = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.img_item, parent, false)
        val holder =  ViewHolder(layout)
        layout.setOnClickListener {
            listner.handleImageClick(it,list[holder.absoluteAdapterPosition],holder.absoluteAdapterPosition)
        }
        holder.save.setOnClickListener {
            holder.save.setImageResource(R.drawable.done)
            listner.handleSave(it,list[holder.absoluteAdapterPosition],true,holder.absoluteAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Constants.map[list[holder.absoluteAdapterPosition].name.toString()] = holder.absoluteAdapterPosition


       if (File(Constants.savePath + "/" + list[position].name).exists()) {
            holder.save.setImageResource(R.drawable.done)
        }
        else{
            holder.save.setImageResource(R.drawable.save_img)
        }
        if(!list[position].exists()){
            list.removeAt(position)
            notifyItemRemoved(position)
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if(list[position].exists()) {
                    Glide.with(context).load(list[position]).into(holder.img)
                }
            }
        }
        }catch (e:Exception){

        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(data: ArrayList<File>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val img: ImageView = itemView.findViewById(R.id.img)
    val save: ImageView = itemView.findViewById(R.id.save)

}
interface ImageClicked{
    fun handleImageClick(view:View,file:File,position: Int)
    fun handleSave(view:View,file:File,sender:Boolean,position: Int)
}