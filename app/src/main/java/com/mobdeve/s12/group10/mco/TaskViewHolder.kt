package com.mobdeve.s12.group10.mco

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.TaskSmallLayoutBinding

class TaskViewHolder(private var viewBinding: TaskSmallLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(task: Task){
        this.viewBinding.txvTaskTitle.text = task.name
        this.viewBinding.txvTaskDate.text = task.date
        this.viewBinding.txvTaskTime.text = task.time
        this.viewBinding.txvShortDesc.text = task.desc
    }
}