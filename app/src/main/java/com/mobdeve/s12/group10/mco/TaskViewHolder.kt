package com.mobdeve.s12.group10.mco

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.TaskSmallLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskViewHolder(private var viewBinding: TaskSmallLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(task: Task){
        this.viewBinding.txvTaskTitle.text = task.name
        this.viewBinding.txvTaskDate.text = FormatDateTime.formatDate(task.date)
        this.viewBinding.txvTaskTime.text = FormatDateTime.formatTime(task.time)
        //this.viewBinding.txvShortDesc.text = task.desc
    }
}