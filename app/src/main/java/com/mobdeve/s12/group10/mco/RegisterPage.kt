package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivityLoginPageBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityRegisterPageBinding

class RegisterPage : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.registerButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            this.startActivity(intent)
            finish()
        }



    }
}