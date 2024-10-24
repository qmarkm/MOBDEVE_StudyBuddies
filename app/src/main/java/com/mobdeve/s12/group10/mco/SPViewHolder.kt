package com.mobdeve.s12.group10.mco

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.SpSmallLayoutBinding

class SPViewHolder(private var viewBinding: SpSmallLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)  {
    fun bindData(sp: StudyPact){
        this.viewBinding.txvSPTitle.text = sp.name
    }
}