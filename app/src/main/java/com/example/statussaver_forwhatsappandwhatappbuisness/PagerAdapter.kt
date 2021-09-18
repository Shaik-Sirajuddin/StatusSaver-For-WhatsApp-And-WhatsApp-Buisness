package com.example.statussaver_forwhatsappandwhatappbuisness

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class PagerAdapter(fragManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragManager,lifecycle) {
   private val imgFrag = ImagesFragment.newInstance()
   private val vidFrag = VideosFragment.newInstance()
   private val savFrag = SavedFragment.newInstance()
   private val abFrag = AboutFragment.newInstance()
    override fun getItemCount(): Int {
        return 4
    }

    override fun containsItem(itemId: Long): Boolean {
        return super.containsItem(itemId)

    }
    override fun createFragment(position: Int): Fragment {
        if(position==0){

            return imgFrag
        }
        else if(position==1) {
            return vidFrag
        }
        else if(position==2){
            return savFrag
        }
        else{
            return abFrag
        }
    }


}