package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private val spList: SPList = SPList(this)
    private lateinit var spAdapter: SPAdapter
    private var spVisible: Boolean = false

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

        viewBinding.rcvSP.visibility = View.GONE

        viewBinding.btnToggleSP.setOnClickListener {
            viewBinding.rcvSP.visibility = View.VISIBLE
            viewBinding.rcvCalendar.visibility = View.GONE
            spVisible = true
        }

        viewBinding.btnToggleTask.setOnClickListener {
            viewBinding.rcvCalendar.visibility = View.VISIBLE
            viewBinding.rcvSP.visibility = View.GONE
            spVisible = false
        }
    }

    override fun onResume() {
        super.onResume()

        //TODO: When coming back, recycler view is invisible -> need to reset via toggle buttons
        var spArray = spList.getLoggedInSP()

        viewBinding.rcvSP.adapter = SPAdapter(spArray, this)
        viewBinding.rcvSP.layoutManager = LinearLayoutManager(this)

        viewBinding.rcvCalendar.adapter = TaskAdapter(this, this, DataGenerator.loadTasks())
        viewBinding.rcvCalendar.layoutManager = LinearLayoutManager(this)

        Log.d("TAG", "YEAH ON RESUmE")
        viewBinding.rcvSP.visibility = View.VISIBLE
    }
}