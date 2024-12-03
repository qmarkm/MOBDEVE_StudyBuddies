package com.mobdeve.s12.group10.mco

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group10.mco.databinding.SpSmallLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SPViewHolder(private var viewBinding: SpSmallLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)  {
    fun bindData(sp: StudyPact){
        this.viewBinding.txvSPTitle.text = sp.name
        this.viewBinding.txvSPDate.text = formatDate(sp.dateTime)
        this.viewBinding.txvSPPlace.text = sp.location
        this.viewBinding.txvStatus.text = sp.status
    }

    fun formatDate(date: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(date))
    }
}