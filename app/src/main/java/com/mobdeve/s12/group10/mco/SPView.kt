package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint as GeoPointFS
import com.mobdeve.s12.group10.mco.databinding.ActivitySpviewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SPView : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpviewBinding
    private lateinit var spArray : ArrayList<StudyPact>
    private lateinit var studyPactAdapter: SPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        
        spArray = ArrayList()
        studyPactAdapter = SPAdapter(spArray, this)
        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)
        viewBinding.rcvStudyPacts.adapter = studyPactAdapter

        fetchSP()

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

        viewBinding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume(){
        super.onResume()
        fetchSP()
    }

    private fun search(query: String) {
        val filteredList = ArrayList<StudyPact>()
        for (sp in spArray) {
            if (sp.name.contains(query, true)) {
                filteredList.add(sp)
            }
        }
        studyPactAdapter.filterList(filteredList)
    }

    private fun fetchSP() {
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        db.collection("studyPacts").whereIn("status", listOf("Upcoming", "Ongoing")).get().addOnSuccessListener { result ->
            spArray.clear() // Clear the list before adding new data
            for (document in result) {
                val timestamp: Timestamp = document.getTimestamp("dateTime") ?: Timestamp.now()
                val spdatetime = inputFormat.format(timestamp.toDate())

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
                spArray.add(sp)
            }

            studyPactAdapter.notifyDataSetChanged()
        }
    }
}
