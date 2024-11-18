package com.mobdeve.s12.group10.mco

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarViewHolder(
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener,
    private val todayDate: String // Get today's date
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(bindingAdapterPosition, dayOfMonth.text.toString())
    }

    fun bind() {
        // Highlight today's date with circular background
        if (dayOfMonth.text.toString() == todayDate) {
            itemView.setBackgroundResource(R.drawable.circle) // Set circular background
        } else {
            itemView.setBackgroundResource(0) // No background for other days
        }
    }
}
