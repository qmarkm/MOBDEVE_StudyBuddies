package com.mobdeve.s12.group10.mco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivitySpcreateBinding

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
            //TODO: Save to SQLite database here
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