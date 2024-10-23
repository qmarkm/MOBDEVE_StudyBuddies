package com.mobdeve.s12.group10.mco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivitySpviewBinding

class SPView : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


    }
}