package com.mobdeve.s12.group10.mco

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Credits to: https://developer.android.com/develop/ui/views/components/pickers
 *      For implementation of Calendar view picker
 */

interface OnDatePass {
    fun onDatePass(data: String)
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var dataPasser: OnDatePass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDatePass
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)

        dataPasser.onDatePass(date)
    }
}