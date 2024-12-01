package com.mobdeve.s12.group10.mco

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpselfBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpupdateBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SPSelf : AppCompatActivity(), OnDatePass, OnTimePass {
    private lateinit var viewBinding: ActivitySpselfBinding
    private lateinit var dialogViewBinding : DialogSpupdateBinding
    private var createDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpselfBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        val inputFormat : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        val spArray = ArrayList<StudyPact>()
        db.collection("studyPacts").get().addOnSuccessListener { result ->
            for (document in result) {
                val timestamp : Timestamp = document.getTimestamp("dateTime") ?: Timestamp.now()
                val spdatetime = inputFormat.format(timestamp.toDate())

                val joiningUsers = document.get("joiningUsers") as? List<Long> ?: listOf()
                val alJoiningUsers = ArrayList(joiningUsers.map { it.toInt() })

                val sp = StudyPact(
                    document.id,
                    document.getString("name") ?: "Error",
                    document.getLong("creator")?.toInt() ?: -1,
                    spdatetime.toString(),
                    document.getString("location") ?: "De La Salle University",
                    document.getString("description") ?: "Error: No values returned",
                    alJoiningUsers
                )
                spArray.add(sp)
            }

            viewBinding.rcvStudyPacts.adapter = SPAdapter(spArray)
        }

        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)

        viewBinding.btnAddSP.setOnClickListener {
            showCreateDialog(this)
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
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

    fun showCreateDialog(activity: AppCompatActivity) {
        val dialogViewBinding = DialogSpupdateBinding.inflate(layoutInflater)

        createDialog = Dialog(this, R.style.DialogStyle)
        createDialog!!.setContentView(dialogViewBinding.root)

        createDialog!!.window?.setBackgroundDrawableResource(R.drawable.button4)

        dialogViewBinding.btnClose.setOnClickListener {
            createDialog!!.dismiss()
        }

        dialogViewBinding.btnUpdate.setOnClickListener {
            if (checkInputFields()) {
                val inputFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

                FirebaseApp.initializeApp(this)
                val db = FirebaseFirestore.getInstance()

                val spname = dialogViewBinding.txvTitleField.text.toString()
                val spcreator: Int = -1 // TODO: Logged-in user's ID here
                val spdate = dialogViewBinding.txvDateField.text.toString()
                val sptime = dialogViewBinding.txvTimeField.text.toString()
                val splocation = dialogViewBinding.txvLocationField.text.toString()
                val spdescription = dialogViewBinding.txvDescField.text.toString()

                val dateTime = "$spdate $sptime"
                val parsedDate: Date = inputFormat.parse(dateTime)
                val spdatetime: Timestamp = Timestamp(parsedDate)

                val arrayJoiningUsers = ArrayList<Int>()
                arrayJoiningUsers.add(spcreator)

                val studyPact = hashMapOf(
                    "name" to spname,
                    "creator" to spcreator,
                    "dateTime" to spdatetime,
                    "location" to splocation,
                    "description" to spdescription,
                    "joiningUsers" to arrayJoiningUsers
                )

                db.collection("studyPacts").add(studyPact).addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Study Pact \"$spname\" created.", Toast.LENGTH_SHORT).show()
                }

                createDialog!!.dismiss()
            } else {
                Toast.makeText(this, "Please input every field", Toast.LENGTH_SHORT).show()
                // TODO: Make borders red
            }

            createDialog!!.dismiss()
        }
        dialogViewBinding.btnUpdate.text = "Create"

        dialogViewBinding.txvTitle.text = "Create Study Pact"

        dialogViewBinding.lytPickDate.setOnClickListener {
            val newFragment = DatePickerFragment(createDialog!!)
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        dialogViewBinding.lytPickTime.setOnClickListener {
            val newFragment = TimePickerFragment(createDialog!!)
            newFragment.show(activity.supportFragmentManager, "timePicker")
        }

        createDialog!!.show()
    }

    override fun onDatePass(data: String) {
        createDialog!!.findViewById<TextView>(R.id.txvDateField)?.text = data
    }

    override fun onTimePass(data: String) {
        createDialog!!.findViewById<TextView>(R.id.txvTimeField)?.text = data
    }

    fun checkInputFields() : Boolean {
        if (createDialog!!.findViewById<TextView>(R.id.txvTitleField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvDateField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvTimeField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvLocationField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvDescField).text.isNullOrBlank())
            return false

        return true
    }
}