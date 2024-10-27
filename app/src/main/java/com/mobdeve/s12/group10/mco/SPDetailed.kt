package com.mobdeve.s12.group10.mco

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Credits to: https://www.youtube.com/watch?v=TMQNvnU0_wE
 *      For Dialog popup
 */

class SPDetailed : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpdetailedBinding
    private lateinit var spTitle: String
    private lateinit var spDesc: String
    private lateinit var spDateTime: String
    private lateinit var spLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpdetailedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        spTitle = intent.getStringExtra("SP_TITLE").toString()
        spDesc = intent.getStringExtra("SP_DESCRIPTION").toString()
        spDateTime = intent.getStringExtra("SP_DATETIME").toString()
        spLocation = intent.getStringExtra("SP_LOCATION").toString()


        viewBinding.txvTitle.text = spTitle
        viewBinding.txvDesc.text = spDesc
        viewBinding.txvDateField.text = formatDate(spDateTime)
        viewBinding.txvTimeField.text = formatTime(spDateTime)
        viewBinding.txvLocationField.text = spLocation

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }

        viewBinding.btnUpdate.setOnClickListener {
            showUpdateDialog()
        }
    }

    fun formatDate(date: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(date))
    }

    fun formatTime(time: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(time))
    }

    fun showUpdateDialog(){
        val dialog = Dialog(this, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_spupdate)

        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.button4)

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)
        btnClose.setOnClickListener{
            dialog.dismiss()
        }

        val btnUpdate = dialog.findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener{
            //TODO: Update parameters here
            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.txvTitleField).text = spTitle
        dialog.findViewById<TextView>(R.id.txvTitle).text = "Update " + spTitle
        dialog.findViewById<TextView>(R.id.txvDateField).text = formatDate(spDateTime)
        dialog.findViewById<TextView>(R.id.txvTimeField).text = formatTime(spDateTime)
        dialog.findViewById<TextView>(R.id.txvLocationField).text = spLocation
        dialog.findViewById<TextView>(R.id.txvDescField).text = spDesc

        dialog.show()
    }
}