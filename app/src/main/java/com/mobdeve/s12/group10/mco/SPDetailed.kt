package com.mobdeve.s12.group10.mco

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpupdateBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Credits to: https://www.youtube.com/watch?v=TMQNvnU0_wE
 *      For Dialog popup
 */

class SPDetailed : AppCompatActivity(), OnDatePass, OnTimePass {
    private lateinit var viewBinding: ActivitySpdetailedBinding
    private lateinit var spTitle: String
    private lateinit var spDesc: String
    private lateinit var spDateTime: String
    private lateinit var spLocation: String
    private var updateDialog: Dialog? = null

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
            showUpdateDialog(this)
        }

        viewBinding.btnStudyPact.setOnClickListener {
            val intent = Intent(this, MainStudyPacts::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        viewBinding.btnAccount.setOnClickListener {
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        viewBinding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
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

    fun showUpdateDialog(activity: AppCompatActivity){
        val dialogViewBinding = DialogSpupdateBinding.inflate(layoutInflater)

        updateDialog = Dialog(this, R.style.DialogStyle)
        updateDialog!!.setContentView(dialogViewBinding.root)

        updateDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.button4)

        val btnClose = dialogViewBinding.btnClose
        btnClose.setOnClickListener{
            updateDialog!!.dismiss()
        }

        val btnUpdate = dialogViewBinding.btnUpdate
        btnUpdate.setOnClickListener{
            //TODO: Update parameters here
            updateDialog!!.dismiss()
        }

        dialogViewBinding.lytPickDate.setOnClickListener {
            val newFragment = DatePickerFragment(updateDialog!!)
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        dialogViewBinding.lytPickTime.setOnClickListener {
            val newFragment = TimePickerFragment(updateDialog!!)
            newFragment.show(activity.supportFragmentManager, "timePicker")
        }

        updateDialog!!.findViewById<TextView>(R.id.txvTitleField).text = spTitle
        updateDialog!!.findViewById<TextView>(R.id.txvTitle).text = "Update " + spTitle
        updateDialog!!.findViewById<TextView>(R.id.txvDateField).text = formatDate(spDateTime)
        updateDialog!!.findViewById<TextView>(R.id.txvTimeField).text = formatTime(spDateTime)
        updateDialog!!.findViewById<TextView>(R.id.txvLocationField).text = spLocation
        updateDialog!!.findViewById<TextView>(R.id.txvDescField).text = spDesc

        updateDialog!!.show()
    }

    override fun onDatePass(data: String) {
        updateDialog!!.findViewById<TextView>(R.id.txvDateField)?.text = data
    }

    override fun onTimePass(data: String) {
        updateDialog!!.findViewById<TextView>(R.id.txvTimeField)?.text = data
    }

    fun checkInputFields() : Boolean {
        if (updateDialog!!.findViewById<TextView>(R.id.txvTitleField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvDateField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvTimeField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvLocationField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvDescField).text.isNullOrBlank())
            return false

        return true
    }
}