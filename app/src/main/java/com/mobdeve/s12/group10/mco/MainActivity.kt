package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnStudyPact.setOnClickListener {
            val intent = Intent(this, MainStudyPacts::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnAccount.setOnClickListener {
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarView::class.java)
            this.startActivity(intent)
        }

        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        val inputFormat : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        val spArray = ArrayList<StudyPact>()
        db.collection("studyPacts").get().addOnSuccessListener { result ->
            for (document in result) {
                val timestamp : Timestamp = document.getTimestamp("dateTime") ?: Timestamp.now()
                val spdatetime = inputFormat.format(timestamp.toDate())

                val joiningUsers = ArrayList(document.get("joiningUsers") as? List<String>)
                if (getLoggedInUserEmail().toString() in joiningUsers) {
                    val sp = StudyPact(
                        document.id,
                        document.getString("name") ?: "Error",
                        document.getString("creator") ?: "dummy@email.com",
                        spdatetime.toString(),
                        document.getString("location") ?: "De La Salle University",
                        document.getString("description") ?: "Error: No values returned",
                        ArrayList(document.get("joiningUsers") as? List<String>),
                        document.getString("status") ?: "Cancelled"
                    )
                    spArray.add(sp)
                }
            }
        }

        viewBinding.rcvSP.adapter = SPAdapter(spArray)
        viewBinding.rcvSP.layoutManager = LinearLayoutManager(this)
        viewBinding.rcvSP.visibility = View.GONE

        viewBinding.rcvCalendar.adapter = TaskAdapter(this, this, DataGenerator.loadTasks())
        viewBinding.rcvCalendar.layoutManager = LinearLayoutManager(this)

        viewBinding.btnToggleSP.setOnClickListener {
            viewBinding.rcvSP.visibility = View.VISIBLE
            viewBinding.rcvCalendar.visibility = View.GONE
        }

        viewBinding.btnToggleTask.setOnClickListener {
            viewBinding.rcvCalendar.visibility = View.VISIBLE
            viewBinding.rcvSP.visibility = View.GONE
        }
    }

    private fun getLoggedInUserEmail(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }
}