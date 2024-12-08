package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivityMainStudyPactsBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Credits to:
 * → https://www.youtube.com/watch?app=desktop&v=ROkKPgXpd1Y
 * → https://github.com/android/platform-samples/blob/main/samples/user-interface/constraintlayout/src/main/res/xml/scene_02.xml
 * → and Mr. Berris
 *
 * For the Motion Layout in activity_main_study_pacts.xml & activity_main_study_pacts_scene.xml
 */

class MainStudyPacts : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainStudyPactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainStudyPactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

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

        viewBinding.rcvStudyPacts.adapter = SPAdapter(spArray, this)
        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)

        viewBinding.btnViewSP.setOnClickListener {
            val intent = Intent(this, SPView::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnSelfSP.setOnClickListener {
            val intent = Intent(this, SPSelf::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun getLoggedInUserEmail(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }
}