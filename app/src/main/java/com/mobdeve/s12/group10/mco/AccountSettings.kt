package com.mobdeve.s12.group10.mco

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.group10.mco.databinding.ActivityAccountSettingsBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityPopoutDeleteBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityPopoutEditbioBinding
import com.mobdeve.s12.group10.mco.databinding.ActivityPopoutEditusernameBinding


class AccountSettings : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAccountSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.logoutButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            this.startActivity(intent)
            finish()
        }

        viewBinding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        viewBinding.editNameButton.setOnClickListener {
            showEditNameDialog()
        }

        viewBinding.editAboutMeButton.setOnClickListener {
            showEditBioDialog()
        }
    }

    private fun showEditNameDialog() {
        val dialogBinding = ActivityPopoutEditusernameBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            dialogBinding.saveNameButton.setOnClickListener{
                val intent = Intent(this, AccountSettings::class.java)
                this.startActivity(intent)
                finish()
            }
            dialogBinding.editNameInput.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
            finish()
            }
        dialog.show()
    }
    private fun showEditBioDialog() {
        val dialogBinding = ActivityPopoutEditbioBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        dialogBinding.saveBioButton.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
            finish()
        }
        dialogBinding.cancelEditBioButton.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
            finish()
        }
        dialog.show()
    }
    private fun showDeleteConfirmationDialog() {
        val dialogBinding = ActivityPopoutDeleteBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        dialogBinding.cancelDeleteButton.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
            finish()
        }
        dialogBinding.confirmDeleteButton.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
            finish()
        }

        dialog.show()
    }
}
