package com.mobdeve.s12.group10.mco

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpcreateBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SPCreate : AppCompatActivity(), OnDatePass, OnTimePass {
    private lateinit var viewBinding: ActivitySpcreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpcreateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.lytPickDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        }

        viewBinding.lytPickTime.setOnClickListener {
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "timePicker")
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }

        viewBinding.btnSubmit.setOnClickListener {
            val inputFormat : SimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

            FirebaseApp.initializeApp(this)
            val db = FirebaseFirestore.getInstance()

            val spname : String = viewBinding.txvTitleField.text.toString()
            val spcreator : Int = -1      //TODO: Logged-in user's ID here
            val spdate : String = viewBinding.txvDateField.text.toString()
            val sptime : String = viewBinding.txvTimeField.text.toString()
            val splocation : String = viewBinding.txvLocationField.text.toString()
            val spdescription : String = viewBinding.txvDescField.text.toString()

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
           /*
            val studyPact = StudyPact (
                name = spname,
                creator = spcreator,
                dateTime = spdatetime,
                location = splocation,
                description = spdescription,
                joiningUsers = spcreator.toString()     //Have the creator auto-join their event
            )*/

            db.collection("studyPacts").add(studyPact).addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Study Pact \"" + spname + "\" created.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }

            finish()
        }
    }

    override fun onDatePass(data: String) {
        viewBinding.txvDateField.text = data
    }

    override fun onTimePass(data: String) {
        viewBinding.txvTimeField.text = data
    }
}