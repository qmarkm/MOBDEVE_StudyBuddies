package com.mobdeve.s12.group10.mco

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.DialogTaskDetailedBinding
import com.mobdeve.s12.group10.mco.databinding.TaskSmallLayoutBinding
import com.mobdeve.s12.group10.mco.Task

class TaskAdapter(
    private val context: Activity,
    private var tasks: ArrayList<Task> // Make tasks mutable
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private lateinit var dialogBinding: DialogTaskDetailedBinding
    private val taskDatabase = TaskDatabase(context) // Access the TaskDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskViewBinding: TaskSmallLayoutBinding = TaskSmallLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(taskViewBinding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(tasks[position])

        holder.itemView.setOnClickListener {
            showEditTaskDialog(tasks[position], position)
        }
    }

    private fun showEditTaskDialog(task: Task, position: Int): Dialog {
        dialogBinding = DialogTaskDetailedBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(dialogBinding.root).create()

        // Populate dialog with current task data
        dialogBinding.editActivityTitleInput.setText(task.name)
        dialogBinding.editActivityDateInput.setText(task.date)
        dialogBinding.editActivityTimeInput.setText(task.time)
        dialogBinding.editActivityDescriptionInput.setText(task.desc)

        dialogBinding.saveActivityButton.setOnClickListener {
            val updatedTask = task.copy(
                name = dialogBinding.editActivityTitleInput.text.toString(),
                date = dialogBinding.editActivityDateInput.text.toString(),
                time = dialogBinding.editActivityTimeInput.text.toString(),
                desc = dialogBinding.editActivityDescriptionInput.text.toString()
            )

            // Update task in database
            taskDatabase.updateTask(updatedTask)
            tasks[position] = updatedTask // Update the local list
            notifyItemChanged(position) // Notify adapter to refresh the item
            dialog.dismiss()
        }

        dialogBinding.deleteActivityButton.setOnClickListener {
            taskDatabase.deleteTask(task) // Pass the entire task object
            tasks.removeAt(position) // Remove task from the list
            notifyItemRemoved(position) // Notify adapter to remove item
            dialog.dismiss()
        }


        dialogBinding.lyvEditDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show((context as FragmentActivity).supportFragmentManager, "datePicker")
        }

        dialogBinding.lyvEditTime.setOnClickListener {
            val newFragment = TimePickerFragment()
            newFragment.show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }

        dialog.show()
        return dialog
    }

    // Method to refresh tasks list
    fun refreshTasks() {
        tasks = ArrayList(taskDatabase.getAllTasks()) // Reload tasks from the database
        notifyDataSetChanged() // Refresh the RecyclerView
    }

    inner class TaskViewHolder(private var taskBinding: TaskSmallLayoutBinding) : RecyclerView.ViewHolder(taskBinding.root) {
        fun bindData(task: Task) {
            taskBinding.txvTaskTitle.text = task.name
            taskBinding.txvTaskDate.text = FormatDateTime.formatDate(task.date)
            taskBinding.txvTaskTime.text = FormatDateTime.formatTime(task.time)
            taskBinding.txvShortDesc.text = task.desc
        }
    }
}
