package com.mobdeve.s12.group10.mco

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group10.mco.databinding.ActivityCalendarViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarView : AppCompatActivity(), CalendarAdapter.OnItemListener, OnDatePass {

    private lateinit var binding: ActivityCalendarViewBinding
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setMonthView()

        binding.btnStudyPact.setOnClickListener {
            startActivity(Intent(this, MainStudyPacts::class.java))
        }

        binding.btnAccount.setOnClickListener {
            startActivity(Intent(this, AccountSettings::class.java))
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setMonthView() {
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
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

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText.isNotEmpty()) {
            val message = "Selected Date $dayText ${monthYearFromDate(selectedDate)}"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDatePass(data: String) {
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = format.parse(data)

        selectedDate = calendar
        setMonthView()
    }

    fun weeklyAction(view: View) {
        startActivity(Intent(this, WeekView::class.java))
    }
}
