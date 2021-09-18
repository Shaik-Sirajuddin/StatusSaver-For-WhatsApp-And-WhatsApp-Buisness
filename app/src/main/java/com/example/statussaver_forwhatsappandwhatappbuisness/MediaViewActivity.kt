package com.example.statussaver_forwhatsappandwhatappbuisness


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


class MediaViewActivity : AppCompatActivity() {
    private lateinit var imgView:ImageView
    private lateinit var save:FloatingActionButton
    private lateinit var share:FloatingActionButton
    private lateinit var playerview:StyledPlayerView
     private lateinit var media:SimpleExoPlayer
     private var tent:Intent? = null
     private var path1:String? = null
    private var flag = false
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        try {
            imgView = findViewById(R.id.imagePlace)
            save = findViewById(R.id.saveButton)
            share = findViewById(R.id.shareButton)
            playerview = findViewById(R.id.videoPlace)
            val intent = intent
            val type = intent.getBooleanExtra("type", true)
            val path = intent.getStringExtra("Uri")
            val uri = Uri.parse(path)
            path1 = intent.getStringExtra("name")
            position = intent.getIntExtra("pos", 0)
            if (File(Constants.savePath + "/" + path1).exists()) {
                save.setImageResource(R.drawable.done)
            }
            if (type) {
                playerview.visibility = View.GONE
                imgView.setImageURI(uri)
            } else {
                flag = true
                imgView.visibility = View.GONE
                playerview.visibility = View.VISIBLE
                media = SimpleExoPlayer.Builder(this).build()
                playerview.player = media
                val mediaItem = MediaItem.fromUri(uri)
                media.addMediaItem(mediaItem)
                media.prepare()
                media.play()
            }
            save.setOnClickListener {
                save()
            }
            share.setOnClickListener {
                share(uri)
            }
        }catch (e:Exception){

        }
    }
    private fun save() {
        try {
            var pat: String = Constants.statusPath
            if (Build.VERSION.SDK_INT >= 30) {
                pat = Constants.status_11
            }
            if (File(Constants.savePath + "/" + path1).exists()) {
                return
            }
            val status = SaveStatus.copy(pat, path1.toString(), Constants.savePath, position, this)
            if (status) {
                val saveImg = findViewById<FloatingActionButton>(R.id.saveButton)
                saveImg.setImageResource(R.drawable.done)
                Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unable To Save File", Toast.LENGTH_SHORT).show()
            }
            Constants.list.add(File(Constants.savePath + "/" + path1))
            Constants.savedCheck = true
            tent = Intent()
            tent?.putExtra("saved", true)
            setResult(RESULT_OK, tent)
        }catch (e:Exception){
        }
    }
    private fun share(uri:Uri){
        try {
            Constants.flag = false
            val intent = Intent(Intent.ACTION_SEND)
            if (path1?.endsWith(".jpg") == true) {
                intent.type = "image/*"
            } else {
                intent.type = "video/*"
            }
            save()
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Constants.savePath + "/" + path1))
            val chooser = Intent.createChooser(intent, "Share using ...")
            startActivity(chooser)
        }catch (e:Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        if(flag) {
            media.pause()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(flag) {
            media.stop()
        }
        finish()
    }
}