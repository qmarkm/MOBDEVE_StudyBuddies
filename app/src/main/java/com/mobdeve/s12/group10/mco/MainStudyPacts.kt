package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivityMainStudyPactsBinding

class MainStudyPacts : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainStudyPactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainStudyPactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.rcvStudyPacts.adapter = SPAdapter(DataGenerator.loadData())
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
}