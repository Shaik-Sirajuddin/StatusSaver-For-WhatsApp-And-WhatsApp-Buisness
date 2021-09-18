package com.example.statussaver_forwhatsappandwhatappbuisness


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout:TabLayout
    private var toast:Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            viewPager = findViewById(R.id.viewPage)
            tabLayout = findViewById(R.id.tab)
            viewPager.adapter= PagerAdapter(supportFragmentManager, lifecycle)
            tabLayout.addTab(tabLayout.newTab().setText("Images").setIcon(R.drawable.images_tab))
            tabLayout.addTab(tabLayout.newTab().setText("Videos").setIcon(R.drawable.video_tab))
            tabLayout.addTab(tabLayout.newTab().setText("Saved").setIcon(R.drawable.saved_tab))
            tabLayout.addTab(tabLayout.newTab().setText("Settings").setIcon(R.drawable.settings_tab))
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
            val refresh = findViewById<ImageView>(R.id.refreshButt)
            refresh.setOnClickListener {
                refresh()
            }
            SaveStatus.updateAutoRefresh()
            if (File(Constants.business).exists()) {
                Constants.businessCheck = true
            }
        }catch (e:Exception){
            Toast.makeText(this,"Error Occurred Restart App",Toast.LENGTH_SHORT).show()
        }
    }
   private fun refresh() {
       toast?.cancel()
       var checker = false
       val frag1: ImagesFragment? =
           supportFragmentManager.findFragmentByTag("f" + 0) as ImagesFragment?
       val frag2: VideosFragment? =
           supportFragmentManager.findFragmentByTag("f" + 1) as VideosFragment?
       val frag3: SavedFragment? =
           supportFragmentManager.findFragmentByTag("f" + 2) as SavedFragment?
       if (frag1?.isVisible == true) {
           frag1.update()
           checker = true
       } else if (frag2?.isVisible == true) {
           frag2.update()
           checker = true
       } else if (frag3?.isVisible == true) {
           frag3.update()
           checker = true
       }
       if (checker) {
          toast =  Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT)
       }
       toast?.show()
   }
   override fun onResume() {
        super.onResume()
        if(Constants.autoRefresh && Constants.flag) {
            val fragment:VideosFragment? = supportFragmentManager.findFragmentByTag("f" +1) as VideosFragment?
            if (fragment?.checkCreated() == true) {
                fragment.notifyVideo()
            }
            val frag =  supportFragmentManager.findFragmentByTag("f" +0) as ImagesFragment?
            if (frag?.checkCreated() == true) {
                frag.notifyImage()
            }
            val frag2 = supportFragmentManager.findFragmentByTag("f" +2) as SavedFragment?
            if(frag2?.checkCreated() == true){
                frag2.notifySaved()
            }
        }else{
            Constants.flag = true
        }
    }

}