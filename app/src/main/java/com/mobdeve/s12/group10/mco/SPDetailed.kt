package com.mobdeve.s12.group10.mco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SPDetailed : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySpdetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpdetailedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.txvTitle.text = intent.getStringExtra("SP_TITLE")
        viewBinding.txvDesc.text = intent.getStringExtra("SP_DESCRIPTION")
        viewBinding.txvDateField.text = intent.getStringExtra("SP_DATETIME")?.let { formatDate(it) }
        viewBinding.txvTimeField.text = intent.getStringExtra("SP_DATETIME")?.let { formatTime(it) }
        viewBinding.txvLocationField.text = intent.getStringExtra("SP_LOCATION")

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }
    }

    fun formatDate(date: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(date))
    }

    fun formatTime(time: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(time))
    }
}