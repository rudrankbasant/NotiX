package com.dscvit.notix.adapters


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R


class CalendarViewHolder(
    itemView: View,

    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var dateOfMonth: TextView? = null
    var dayOfMonth: TextView? = null


    init {
        dateOfMonth = itemView.findViewById(R.id.monthDate)
        dayOfMonth = itemView.findViewById(R.id.monthDay)
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}