package com.mobdeve.s12.group10.mco

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpupdateBinding
import java.text.SimpleDateFormat
import java.util.Date
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
    private lateinit var spId: String
    private lateinit var spStatus: String
    private var updateDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpdetailedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        spId = intent.getStringExtra("SP_ID").toString()
        spTitle = intent.getStringExtra("SP_TITLE").toString()
        spDesc = intent.getStringExtra("SP_DESCRIPTION").toString()
        spDateTime = intent.getStringExtra("SP_DATETIME").toString()
        spLocation = intent.getStringExtra("SP_LOCATION").toString()
        spStatus = intent.getStringExtra("SP_STATUS").toString()

        viewBinding.txvTitle.text = spTitle
        viewBinding.txvDesc.text = spDesc
        viewBinding.txvDateField.text = formatDate(spDateTime)
        viewBinding.txvTimeField.text = formatTime(spDateTime)
        viewBinding.txvLocationField.text = spLocation
        viewBinding.txvStatus.text = "Status: " + spStatus

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }

        viewBinding.btnCreate.setOnClickListener {
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

        if (spStatus == "Cancelled" || spStatus == "Finished") {
            viewBinding.btnJoin.visibility = View.GONE
            viewBinding.btnCreate.visibility = View.GONE
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

        //Unsure why it cannot be renamed to btnUpdate, thinking it's because dialog_spupdate.xml and dialog_spcreate.xml are linked
        val btnUpdate = dialogViewBinding.btnCreate
        btnUpdate.setOnClickListener{
            //Submit request to update Study Pact
            if (checkInputFields()) {
                val inputFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

                FirebaseApp.initializeApp(this)
                val db = FirebaseFirestore.getInstance()

                val dateTime = "${dialogViewBinding.txvDateField.text.toString()} ${dialogViewBinding.txvTimeField.text.toString()}"
                val parsedDate: Date = inputFormat.parse(dateTime)

                spTitle = dialogViewBinding.txvTitleField.text.toString()
                spDesc = dialogViewBinding.txvDescField.text.toString()
                val spDateTimeTs : Timestamp = Timestamp(parsedDate)
                spLocation = dialogViewBinding.txvLocationField.text.toString()

                val studyPact = hashMapOf(
                    "name" to spTitle,
                    "dateTime" to spDateTimeTs,
                    "location" to spLocation,
                    "description" to spDesc,
                )

                db.collection("studyPacts").document(spId).update(studyPact as Map<String, Any>).addOnSuccessListener {
                    Toast.makeText(this, "Study Pact \"$spTitle\" updated.", Toast.LENGTH_SHORT).show()
                }

                updateDialog!!.dismiss()
            } else {
                Toast.makeText(this, "Please input every field", Toast.LENGTH_SHORT).show()
            }
        }


        /**
         * Credits for delete alert from https://stackoverflow.com/questions/59340099/how-to-set-confirm-delete-alertdialogue-box-in-kotlin
         */
        dialogViewBinding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to permanently cancel ${spTitle}?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    //TODO: Delete logic here
                    FirebaseApp.initializeApp(this)
                    val db = FirebaseFirestore.getInstance()

                    val cancel = hashMapOf(
                        "status" to "Cancelled"
                    )

                    db.collection("studyPacts").document(spId).update(cancel as Map<String, Any>).addOnSuccessListener {
                        Toast.makeText(this, "Study Pact \"$spTitle\" cancelled.", Toast.LENGTH_SHORT).show()
                    }

                    updateDialog!!.dismiss()
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
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