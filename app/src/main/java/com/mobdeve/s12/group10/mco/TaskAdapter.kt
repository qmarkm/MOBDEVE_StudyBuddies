package com.mobdeve.s12.group10.mco

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.DialogTaskDetailedBinding
import com.mobdeve.s12.group10.mco.databinding.TaskSmallLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

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

        // Convert the task date to a format suitable for display
        val formattedDate = try {
            val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Database format
            val readableDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) // User-friendly format
            val parsedDate = dbDateFormat.parse(task.date) // Parse the task date in "yyyy-MM-dd" format
            readableDateFormat.format(parsedDate!!) // Convert to "MMMM dd, yyyy" format for display
        } catch (e: Exception) {
            e.printStackTrace()
            task.date // If parsing fails, fallback to the original task date
        }

        // Populate dialog with current task data
        dialogBinding.editActivityTitleInput.setText(task.name)
        dialogBinding.editActivityDateInput.setText(formattedDate)
        dialogBinding.editActivityTimeInput.setText(task.time)
        dialogBinding.editActivityDescriptionInput.setText(task.desc)

        dialogBinding.saveActivityButton.setOnClickListener {
            try {
                // Ensure the date is in "yyyy-MM-dd" format before saving
                val inputDate = dialogBinding.editActivityDateInput.text.toString()
                val formattedDateToSave = try {
                    val readableDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val parsedDate = readableDateFormat.parse(inputDate)
                    dbDateFormat.format(parsedDate!!)
                } catch (e: Exception) {
                    inputDate // Fallback to original input if parsing fails
                }

                // Copy the task and update fields
                val updatedTask = task.copy(
                    name = dialogBinding.editActivityTitleInput.text.toString(),
                    date = formattedDateToSave, // Save date in "yyyy-MM-dd" format
                    time = dialogBinding.editActivityTimeInput.text.toString(),
                    desc = dialogBinding.editActivityDescriptionInput.text.toString()
                )

                // Update task in the database
                taskDatabase.updateTask(updatedTask)
                tasks[position] = updatedTask // Update the local list
                notifyItemChanged(position) // Notify adapter to refresh the item

                // Close the dialog
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    dialogBinding.root.context,
                    "Error saving task: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        dialogBinding.deleteActivityButton.setOnClickListener {
            taskDatabase.deleteTask(task) // Pass the entire task object
            tasks.removeAt(position) // Remove task from the list
            notifyItemRemoved(position) // Notify adapter to remove item
            dialog.dismiss()
        }


        dialogBinding.lyvEditDate.setOnClickListener {
            val newFragment = TaskDatePickerFragment()
            newFragment.show((context as FragmentActivity).supportFragmentManager, "datePicker")
        }

        dialogBinding.lyvEditTime.setOnClickListener {
            val newFragment = TaskTimePickerFragment()
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
