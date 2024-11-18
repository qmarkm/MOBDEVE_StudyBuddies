package com.mobdeve.s12.group10.mco

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtil {

    var selectedDate: Calendar? = null

    fun formattedDate(date: Calendar): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date.time)
    }

    fun formattedTime(time: Calendar): String {
        val formatter = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return formatter.format(time.time)
    }

    fun monthYearFromDate(date: Calendar): String {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return formatter.format(date.time)
    }

    fun daysInMonthArray(date: Calendar): List<Calendar?> {
        val daysInMonthArray = mutableListOf<Calendar?>()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)

        val firstOfMonth = Calendar.getInstance()
        firstOfMonth.set(year, month, 1)

        val daysInMonth = firstOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK) - 1

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(null)
            } else {
                val day = Calendar.getInstance()
                day.set(year, month, i - dayOfWeek)
                daysInMonthArray.add(day)
            }
        }
        return daysInMonthArray
    }

    fun daysInWeekArray(selectedDate: Calendar): List<Calendar> {
        val days = mutableListOf<Calendar>()
        val current = sundayForDate(selectedDate)
        val endDate = Calendar.getInstance().apply {
            time = current.time
            add(Calendar.DAY_OF_YEAR, 7)
        }

        while (current.before(endDate)) {
            days.add(current.clone() as Calendar)
            current.add(Calendar.DAY_OF_YEAR, 1)
        }
        return days
    }

    private fun sundayForDate(current: Calendar): Calendar {
        val temp = current.clone() as Calendar
        while (temp.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            temp.add(Calendar.DAY_OF_YEAR, -1)
        }
        return temp
    }
}
