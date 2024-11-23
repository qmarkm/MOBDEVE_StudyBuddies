package com.mobdeve.s12.group10.mco

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpcreateBinding
import java.text.SimpleDateFormat
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
            val outputFormat : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            val inputFormat : SimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

            //TODO: Save to SQLite database here
            val db = Firebase.firestore

            val spname : String = viewBinding.txvTitleField.text.toString()
            val spcreator : Int = -1      //TODO: Logged-in user's ID here
            val spdate : String = viewBinding.txvDateField.text.toString()
            val sptime : String = viewBinding.txvTimeField.text.toString()
            val splocation : String = viewBinding.txvLocationField.text.toString()
            val spdescription : String = viewBinding.txvDescField.text.toString()

            val dateTime = "$spdate $sptime"
            val spdatetime = outputFormat.format(inputFormat.parse(dateTime))

            Log.d("TAG", "spname: $spname")
            Log.d("TAG", "spcreator: $spcreator")
            Log.d("TAG", "spdatetime: $spdatetime")
            Log.d("TAG", "splocation: $splocation")
            Log.d("TAG", "spdescription: $spdescription")


            val studyPact = StudyPact (
                id = spcreator,
                name = spname,
                creator = spcreator,
                dateTime = spdatetime,
                location = splocation,
                description = spdescription,
                joiningUsers = spcreator.toString()     //Have the creator auto-join their event
            )

            db.collection("studyPacts").add(studyPact).addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
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