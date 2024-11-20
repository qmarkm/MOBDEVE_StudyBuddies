package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.DialogTaskDetailedBinding
import com.mobdeve.s12.group10.mco.databinding.TaskSmallLayoutBinding

class TaskAdapter(private val context : Context, private val fragmentActivity: FragmentActivity, private val tasks: ArrayList<Task>): RecyclerView.Adapter<TaskViewHolder>(), OnDatePass, OnTimePass {
    private lateinit var dialogBinding: DialogTaskDetailedBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskViewBinding: TaskSmallLayoutBinding = TaskSmallLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(taskViewBinding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(tasks.get(position))

        holder.itemView.setOnClickListener{
            showEditTaskDialog(context, tasks.get(position))
        }
    }

    private fun showEditTaskDialog(context: Context, task: Task) {
        dialogBinding = DialogTaskDetailedBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialog.dismiss()

        dialogBinding.saveActivityButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.deleteActivityButton.setOnClickListener {
            //TODO: Delete
            //TODO: Update RecyclerView to remove view
            dialog.dismiss()
        }

        dialogBinding.lyvEditDate.setOnClickListener{
            val newFragment = DatePickerFragment()
            newFragment.show(fragmentActivity.supportFragmentManager, "datePicker")
        }

        dialogBinding.lyvEditTime.setOnClickListener{
            val newFragment = TimePickerFragment()
            newFragment.show(fragmentActivity.supportFragmentManager, "timePicker")
        }

        dialogBinding.editActivityTitleInput.setText(task.name)
        dialogBinding.editActivityDateInput.setText(FormatDateTime.formatDate(task.date))
        dialogBinding.editActivityTimeInput.setText(FormatDateTime.formatTime(task.time))
        dialogBinding.editActivityDescriptionInput.setText(task.desc)

        dialog.show()
    }

    override fun onDatePass(data: String) {
        dialogBinding.editActivityDateInput.setText(data)
    }

    override fun onTimePass(data: String) {
        dialogBinding.editActivityTimeInput.setText(data)
    }
}