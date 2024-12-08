package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group10.mco.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        viewBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        checkIfUserIsLoggedIn()

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if the user is already logged in
        checkIfUserIsLoggedIn()

        // Login button click listener
        viewBinding.loginButton.setOnClickListener {
            val email = viewBinding.emailInput.text.toString().trim()
            val password = viewBinding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        // Register link click listener
        viewBinding.registerLink.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }

    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save user info to SharedPreferences
                    saveUserInfo(email)

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainStudyPacts::class.java)   //TODO: Change this to home
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserInfo(email: String)
    {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserEmail", email)
        editor.apply()  // Save changes asynchronously
    }

    private fun checkIfUserIsLoggedIn()///checks if the user is till logged in (nagexit nalang ng app without logging out)
    {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null)
        {
            // User is already logged in, redirect to the MainStudyPacts activity
            val intent = Intent(this, MainStudyPacts::class.java)
            startActivity(intent)
            finish()
        }
    }
}
