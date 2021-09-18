package com.example.statussaver_forwhatsappandwhatappbuisness

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import java.util.*

class SplashScreen : AppCompatActivity() {
    private val REQUEST_PERMISSIONS = 1234
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var progressBar:ProgressBar
    private val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R){
                if(Environment.isExternalStorageManager()){
                    progressBar.visibility = View.VISIBLE
                    next()
                }
                else{
                    Toast.makeText(this,"Permission is required to run the App",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private val resultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){
                progressBar.visibility = View.VISIBLE
                next()
            }
            else{
                Toast.makeText(this,"Permission is required to run the App",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        if (!arePermissionDenied()) {
            next()
            return
        }
        progressBar = findViewById(R.id.progress_bar)
        val button: Button = findViewById(R.id.per)
        button.setOnClickListener {
            android11()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.R) {
                requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS)
            }
            else{
                val textView:TextView = findViewById(R.id.pertext)
                progressBar.visibility = View.GONE
                textView.visibility = View.VISIBLE
                button.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!arePermissionDenied()){
            next()
        }
    }
    private fun android11(){
        try {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse(String.format("package:%s", arrayOf(applicationContext.packageName)))
            resultLauncher.launch(intent)
        }catch (e:Exception){
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            resultLauncher1.launch(intent)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS && grantResults.isNotEmpty()) {
            if (arePermissionDenied()) {
                // Clear Data of Application, So that it can request for permissions again
                (Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE)) as ActivityManager).clearApplicationUserData()
                recreate()
            } else {
                next()
            }
        }
    }

    private operator fun next() {
        try {
            handler.postDelayed({
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }, 1000)
        }catch (e:Exception){

        }
    }

    private fun arePermissionDenied(): Boolean {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.R) {
            for (permissions in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        permissions
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            }
        }else{
            if(!Environment.isExternalStorageManager())return true
        }
        return false
    }
}