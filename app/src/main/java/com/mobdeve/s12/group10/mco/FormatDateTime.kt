package com.mobdeve.s12.group10.mco

import java.text.SimpleDateFormat
import java.util.Locale

class FormatDateTime {
    companion object {
        fun formatDate(date: String): String{
            val dbFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val readableFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

            return readableFormat.format(dbFormat.parse(date))
        }

        fun formatTime(time: String): String{
            val dbFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val readableFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            return readableFormat.format(dbFormat.parse(time))
        }
    }
}