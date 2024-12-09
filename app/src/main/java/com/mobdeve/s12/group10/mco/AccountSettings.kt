package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.*

class AccountSettings : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAccountSettingsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Retrieve user email from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPreferences.getString("loggedInUserEmail", "") ?: ""

        // Load initial data
        loadUserData()

        // Initialize logout button
        viewBinding.logoutButton.setOnClickListener {
            logoutUser()
        }

        // Initialize delete account button
        viewBinding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Initialize edit username button
        viewBinding.editNameButton.setOnClickListener {
            showEditUsernameDialog()
        }

        // Initialize edit bio button
        viewBinding.editAboutMeButton.setOnClickListener {
            showEditBioDialog()
        }
    }

    private fun loadUserData() {
        // Retrieve email from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (savedEmail != null) {
            // Display email immediately
            viewBinding.emailText.text = savedEmail

            // Fetch username and bio from the "accounts" collection
            firestore.collection("accounts")
                .whereEqualTo("email", savedEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val username = document.getString("username") ?: "No Username Provided"
                        val bio = document.getString("bio") ?: "No Bio Available"

                        // Update UI elements for username and bio
                        viewBinding.nameLabel.text = username
                        viewBinding.aboutMeText.text = bio
                    } else {
                        Toast.makeText(this, "No user data found in 'accounts' collection.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error loading user info: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

            // Fetch additional account status or fields from "accounts" collection
            firestore.collection("accounts")
                .whereEqualTo("email", savedEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val isDeleted = document.getBoolean("isDeleted") ?: false

                        // Check account status
                        if (isDeleted) {
                            Toast.makeText(this, "This account is marked as deleted.", Toast.LENGTH_SHORT).show()
                            logoutUser() // Redirect to login if account is deleted
                        }
                    } else {
                        Toast.makeText(this, "No user data found in 'accounts' collection.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error loading account data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle missing email in SharedPreferences
            Toast.makeText(this, "No email found. Redirecting to login.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all stored data
        editor.apply()

        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()

        // Redirect to LoginPage
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun showEditUsernameDialog() {
        val dialogBinding = ActivityPopoutEditusernameBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.saveNameButton.setOnClickListener {
            val newUsername = dialogBinding.editNameInput.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                updateFirestoreField("username", newUsername, "accounts") {
                    viewBinding.nameLabel.text = newUsername // Update the UI immediately
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid username.", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.cancelEditNameButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditBioDialog() {
        val dialogBinding = ActivityPopoutEditbioBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.saveBioButton.setOnClickListener {
            val newBio = dialogBinding.editBioInput.text.toString().trim()
            if (newBio.isNotEmpty()) {
                updateFirestoreField("bio", newBio, "accounts") {
                    viewBinding.aboutMeText.text = newBio // Update the UI immediately
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid bio.", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.cancelEditBioButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBinding = ActivityPopoutDeleteBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.confirmDeleteButton.setOnClickListener {
            setAccountDeleted()
            dialog.dismiss()
        }

        dialogBinding.cancelDeleteButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setAccountDeleted() {
        firestore.collection("accounts")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update("isDeleted", true)
                        .addOnSuccessListener {
                            deleteUserAccount()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to delete account.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Account retrieval failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteUserAccount() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateFirestoreField(field: String, value: String, collection: String, onSuccess: () -> Unit) {
        firestore.collection(collection)
            .whereEqualTo("email", userEmail) // Ensure the query matches the correct user
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    val document = documents.documents[0]
                    document.reference.update(field, value)
                        .addOnSuccessListener {
                            onSuccess() // Update the UI immediately
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update $field: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }else {
                    Toast.makeText(this, "User document not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update $field: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
