package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivityCalendarViewBinding
import com.mobdeve.s12.group10.mco.databinding.DialogTaskCreateBinding
import com.mobdeve.s12.group10.mco.databinding.DialogTaskDetailedBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.mobdeve.s12.group10.mco.Task

class CalendarView : AppCompatActivity(), CalendarAdapter.OnItemListener, OnDatePass, OnTimePass {

    private lateinit var binding: ActivityCalendarViewBinding
    private lateinit var dialogBinding: DialogTaskCreateBinding
    private lateinit var taskDatabase: TaskDatabase

    companion object {
        var selectedDate: Calendar = Calendar.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize TaskDatabase
        taskDatabase = TaskDatabase(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load all tasks from the database
        loadTasks()

        binding.btnCreate.setOnClickListener {
            showCreateTaskDialog()
        }

        setMonthView()

        binding.btnStudyPact.setOnClickListener {
            val intent = Intent(this, MainStudyPacts::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        binding.btnAccount.setOnClickListener {
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        binding.btnShowAllTasks.setOnClickListener {
            showAllTasks()
        }
    }

    private fun setMonthView() {
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)

        // Get today's date as string
        val today = Calendar.getInstance()
        val todayDate = today.get(Calendar.DAY_OF_MONTH).toString() // Get today's day

        // Pass todayDate to the adapter
        val calendarAdapter = CalendarAdapter(daysInMonth, this, todayDate)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: Calendar): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH)

        date.set(Calendar.DAY_OF_MONTH, 1)
        val dayOfWeek = date.get(Calendar.DAY_OF_WEEK) - 1

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: Calendar): String {
        val month = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = date.get(Calendar.YEAR)
        return "$month $year"
    }

    fun previousMonthAction(view: View) {
        selectedDate.add(Calendar.MONTH, -1)
        setMonthView()
    }

    fun nextMonthAction(view: View) {
        selectedDate.add(Calendar.MONTH, 1)
        setMonthView()
    }

    private fun filterTasksByDate(tasks: ArrayList<Task>, date: String): ArrayList<Task> {
        return tasks.filter { it.date == date } as ArrayList<Task>
    }

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText.isNotEmpty()) {
            try {
                // Construct the selected date in the format used in the task data: "yyyy-MM-dd"
                val selectedDate = "${monthYearFromDate(selectedDate)} $dayText"
                val dateFormat = SimpleDateFormat("MMMM yyyy dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(selectedDate)

                // Convert to "yyyy-MM-dd" format for task filtering
                val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate!!)

                // Load tasks from the database filtered by date
                val filteredTasks: List<Task> = taskDatabase.getTasksByDate(dateString)

                if (filteredTasks.isEmpty()) {
                    // Handle the case where no tasks exist for the selected date
                    Toast.makeText(this, "No tasks for $dateString", Toast.LENGTH_SHORT).show()
                }

                // Update the RecyclerView with the filtered task list
                binding.rcvTasks.adapter = TaskAdapter(this, filteredTasks as ArrayList<Task>)
                binding.rcvTasks.layoutManager = LinearLayoutManager(this)

            } catch (e: Exception) {
                // Catch any issues and show a meaningful error message
                e.printStackTrace()
                Toast.makeText(this, "Error parsing date: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAllTasks() {
        // Load all tasks from the database
        val allTasks = taskDatabase.getAllTasks()
        binding.rcvTasks.adapter = TaskAdapter(this, allTasks)
        binding.rcvTasks.layoutManager = LinearLayoutManager(this)

        // Show a confirmation message
        Toast.makeText(this, "Showing all tasks", Toast.LENGTH_SHORT).show()
    }

    fun showCreateTaskDialog() {
        dialogBinding = DialogTaskCreateBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialog.dismiss()

        dialogBinding.lyvEditDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        }

        dialogBinding.lyvEditTime.setOnClickListener {
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "timePicker")
        }

        dialogBinding.saveActivityButton.setOnClickListener {
            // Get the task details from the dialog
            val task = Task(
                id = 0, // ID is generated by the database
                name = dialogBinding.editActivityTitleInput.text.toString(),
                date = dialogBinding.editActivityDateInput.text.toString(),
                time = dialogBinding.editActivityTimeInput.text.toString(),
                desc = dialogBinding.editActivityDescriptionInput.text.toString()
            )

            // Insert the task into the database
            taskDatabase.addTask(task)

            // Refresh the task list
            loadTasks()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadTasks() {
        val tasks = taskDatabase.getAllTasks()
        binding.rcvTasks.adapter = TaskAdapter(this, tasks)
        binding.rcvTasks.layoutManager = LinearLayoutManager(this)
    }

    override fun onDatePass(data: String) {
        dialogBinding.editActivityDateInput.setText(data)
    }

    override fun onTimePass(data: String) {
        dialogBinding.editActivityTimeInput.setText(data)
    }
}
