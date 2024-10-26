package com.mobdeve.s12.group10.mco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding

class SPDetailed : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpdetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpdetailedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.txvTitle.text = intent.getStringExtra("SP_TITLE")

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }
    }
}