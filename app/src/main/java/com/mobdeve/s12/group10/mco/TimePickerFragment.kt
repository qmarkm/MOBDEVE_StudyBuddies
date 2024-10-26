package com.mobdeve.s12.group10.mco

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Credits to: https://developer.android.com/develop/ui/views/components/pickers
 *      For implementation of Time view picker
 */

interface OnTimePass {
    fun onTimePass(data: String)
}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    lateinit var dataPasser: OnTimePass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnTimePass
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val time = timeFormat.format(calendar.time)

        dataPasser.onTimePass(time)
    }
}