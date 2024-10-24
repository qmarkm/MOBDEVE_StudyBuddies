package com.mobdeve.s12.group10.mco

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.SpSmallLayoutBinding

class SPAdapter(private val studyPacts: ArrayList<StudyPact>): RecyclerView.Adapter<SPViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SPViewHolder {
        val spViewBinding: SpSmallLayoutBinding = SpSmallLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SPViewHolder(spViewBinding)
    }

    override fun getItemCount(): Int {
        return studyPacts.size
    }

    override fun onBindViewHolder(holder: SPViewHolder, position: Int) {
        holder.bindData(studyPacts.get(position))
    }
}