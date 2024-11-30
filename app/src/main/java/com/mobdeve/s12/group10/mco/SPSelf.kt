package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpselfBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SPSelf : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpselfBinding

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

                val sp = StudyPact(
                    document.id,
                    document.getString("name") ?: "Error",
                    document.getLong("creator")?.toInt() ?: -1,
                    spdatetime.toString(),
                    document.getString("location") ?: "De La Salle University",
                    document.getString("description") ?: "Error: No values returned",
                    document.getString("joiningUsers") ?: "-1"
                )
                spArray.add(sp)
            }

            viewBinding.rcvStudyPacts.adapter = SPAdapter(spArray)
        }

        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)

        viewBinding.btnAddSP.setOnClickListener {
            val intent = Intent(this, SPCreate::class.java)
            this.startActivity(intent)
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
}