package com.example.statussaver_forwhatsappandwhatappbuisness


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentManager
import java.io.File


class AboutFragment : Fragment(R.layout.fragment_about) {
    private lateinit var send: Button
    lateinit var content:EditText
    private lateinit var whatsApp:TextView
    private lateinit var refreshState:SwitchCompat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send = view.findViewById(R.id.sendMail)
        content = view.findViewById(R.id.suggestContent)
        whatsApp =view.findViewById(R.id.switchWhatsapp)
        refreshState = view.findViewById(R.id.autoRefreshButton)
        refreshState.isChecked = SaveStatus.isAutoRefreshEnabled()

        send.setOnClickListener {
            sendMail()
        }
        if(Constants.businessCheck){
            whatsApp.setText(R.string.switch_whatsapp)
        }
        else{
            whatsApp.setText(R.string.switch_whatsapp_business)
        }
        whatsApp.setOnClickListener{
            whats()
        }
        refreshState.setOnCheckedChangeListener { _, b ->
            if(b){
                if(File(Constants.refreshCheck).exists()){
                    File(Constants.refreshCheck).delete()
                }
                SaveStatus.updateAutoRefresh()
            }
            else{
                if(!File(Constants.refreshCheck).exists()){
                    File(Constants.refreshCheck).mkdirs()
                }
                SaveStatus.updateAutoRefresh()
            }
        }

    }

    private fun whats() {
        if(Constants.businessCheck){
            Constants.businessCheck = false
            if(File(Constants.business).exists()){
                File(Constants.business).delete()
            }
            whatsApp.setText(R.string.switch_whatsapp_business)

        }
        else{
            Constants.businessCheck = true
            if(!File(Constants.business).exists()){
                File(Constants.business).mkdirs()
            }
            whatsApp.setText(R.string.switch_whatsapp)
        }
        val manager:FragmentManager? = fragmentManager
        val imgFrag = manager?.findFragmentByTag("f"+0) as ImagesFragment?
        val vidFrag = manager?.findFragmentByTag("f"+1) as VideosFragment?
        imgFrag?.whatChanged()
        vidFrag?.whatChanged()
    }

    private fun sendMail() {
       try {
            val text = content.editableText.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL,arrayOf(Constants.mail))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Status Saver Suggestion")
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }catch (e:Exception){}
    }

    companion object {
        private var frag:AboutFragment? = null
        fun newInstance(): AboutFragment{
            if(frag==null){
                frag = AboutFragment()
            }
            return frag as AboutFragment
        }
    }
}