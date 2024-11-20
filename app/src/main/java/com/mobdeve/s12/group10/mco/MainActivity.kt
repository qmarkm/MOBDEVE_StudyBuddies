package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivityMainBinding

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

        viewBinding.rcvSP.adapter = SPAdapter(DataGenerator.loadData())
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
}