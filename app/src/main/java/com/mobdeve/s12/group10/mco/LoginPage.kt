package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivityLoginPageBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityMainBinding

class LoginPage : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.loginButton.setOnClickListener {
            val intent = Intent(this, MainStudyPacts::class.java)
            this.startActivity(intent)
            finish()
        }
        viewBinding.registerLink.setOnClickListener {
            // Redirect to the registration page
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }
}