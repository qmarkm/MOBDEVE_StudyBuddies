package com.mobdeve.s12.group10.mco

import java.text.SimpleDateFormat
import java.util.Locale

class FormatDateTime {
    companion object {
        private val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Database date format
        private val readableDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) // User-friendly date format
        private val dbTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // Database time format
        private val readableTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // User-friendly time format

        fun formatDate(date: String): String {
            return try {
                val parsedDate = dbDateFormat.parse(date) // Parse date from DB format
                readableDateFormat.format(parsedDate!!) // Convert to readable format
            } catch (e: Exception) {
                date // Fallback: Return original string if parsing fails
            }
        }

        fun formatTime(time: String): String {
            return try {
                val parsedTime = dbTimeFormat.parse(time) // Parse time from DB format
                readableTimeFormat.format(parsedTime!!) // Convert to readable format
            } catch (e: Exception) {
                time // Fallback: Return original string if parsing fails
            }
        }
    }
}
