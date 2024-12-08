package com.mobdeve.s12.group10.mco


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s12.group10.mco.databinding.ActivityLoginPageBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityRegisterPageBinding
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class RegisterPage : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRegisterPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewBinding.registerButton.setOnClickListener {
            val name = viewBinding.nameInput.text.toString().trim()
            val email = viewBinding.emailInput.text.toString().trim()
            val password = viewBinding.passwordInput.text.toString().trim()
            val confirmPassword = viewBinding.confirmPasswordInput.text.toString().trim()

            if (password == confirmPassword)
            {
                registerUser(name, email, password)
            }
            else
            {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    saveUserToFirestore(userId, name, email)
                } else
                {
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String, name: String, email: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        db.collection("accounts")
            .document(userId)
            .set(user)
            .addOnSuccessListener{
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}