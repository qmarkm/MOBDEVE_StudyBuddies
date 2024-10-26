package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivitySpselfBinding

class SPSelf : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpselfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpselfBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.rcvStudyPacts.adapter = SPAdapter(DataGenerator.loadData())
        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)

        viewBinding.btnAddSP.setOnClickListener {
            val intent = Intent(this, SPCreate::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }
    }
}