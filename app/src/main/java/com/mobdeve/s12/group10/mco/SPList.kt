package com.mobdeve.s12.group10.mco

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.util.GeoPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.firebase.firestore.GeoPoint as GeoPointFS

class SPList(private val context: Context) {
    private val inputFormat : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    private lateinit var spArray : ArrayList<StudyPact>

    public fun getLoggedInSP() : ArrayList<StudyPact> {
        spArray = ArrayList<StudyPact>()

        FirebaseApp.initializeApp(context)
        val db = FirebaseFirestore.getInstance()

        this.spArray = ArrayList<StudyPact>()
        db.collection("studyPacts").get().addOnSuccessListener { result ->
            for (document in result) {
                val timestamp : Timestamp = document.getTimestamp("dateTime") ?: Timestamp.now()
                val today = Calendar.getInstance()
                val spdatetime = inputFormat.format(timestamp.toDate())

                val joiningUsers = ArrayList(document.get("joiningUsers") as? List<String>)
                if (getLoggedInUserEmail().toString() in joiningUsers && (timestamp.toDate()).after(today.time)) {
                    val sp = StudyPact(
                        document.id,
                        document.getString("name") ?: "Error",
                        document.getString("creator") ?: "dummy@email.com",
                        spdatetime.toString(),
                        (document.get("location") ?: GeoPointFS(4.5648, 120.9932)) as GeoPointFS,
                        document.getString("description") ?: "Error: No values returned",
                        ArrayList(document.get("joiningUsers") as? List<String>),
                        document.getString("status") ?: "Cancelled"
                    )
                    this.spArray.add(sp)
                }
            }
            sortSP()
        }

        return this.spArray
    }

    private fun getLoggedInUserEmail(): String? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }

    private fun sortSP() {
        for (i in 1 until this.spArray.size) {
            var key = this.spArray.get(i)
            var j = i - 1

            while (j >= 0 && inputFormat.parse(this.spArray.get(j).dateTime) > inputFormat.parse(key.dateTime)) {
                this.spArray[j+1] = this.spArray[j]
                j -= 1
            }

            this.spArray[j+1] = key
        }
    }
}