package com.dscvit.notix.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import java.time.LocalDate


class CalendarAdapter(
    val context: Context,
    days: ArrayList<LocalDate?>?,
    private val onDateClickInterface: OnDateClickInterface
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {


    private var days: ArrayList<LocalDate?>? = days
    var cardViewList: ArrayList<LinearLayout> = ArrayList()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateOfMonth = itemView.findViewById<TextView>(com.dscvit.notix.R.id.monthDate)
        val dayOfMonth = itemView.findViewById<TextView>(com.dscvit.notix.R.id.monthDay)
        val calendarCell = itemView.findViewById<LinearLayout>(com.dscvit.notix.R.id.calenderCell)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.dscvit.notix.R.layout.itemview_calendar_date, parent, false)
        val layoutParams: ViewGroup.LayoutParams = itemView.layoutParams
        layoutParams.height = (parent.height)
        itemView.setBackgroundColor(0x00FFFFFF)
        //val itemView = LayoutInflater.from(parent.context).inflate(com.dscvit.notix.R.layout.itemview_analytics, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date: LocalDate? = days!![position]
        if (date == LocalDate.now()) {
            holder.calendarCell.setBackgroundResource(R.drawable.calendar_date_bg_selected)
        }
        cardViewList.add(holder.calendarCell)
        if (date == null) {
            holder.dayOfMonth?.text = ""
            holder.dateOfMonth?.text = ""
        } else {
            holder.dayOfMonth?.text = date.dayOfWeek.toString()[0].toString()
            holder.dateOfMonth?.text = date.dayOfMonth.toString()
        }
        holder.itemView.setOnClickListener {
            onDateClickInterface.onItemClick(position, date)
            for (i in cardViewList) {
                i.setBackgroundResource(R.drawable.calendar_date_bg)
            }
            holder.calendarCell.setBackgroundResource(R.drawable.calendar_date_bg_selected)
        }


    }

    override fun getItemCount(): Int {
        return days!!.size
    }

}

interface OnDateClickInterface {
    fun onItemClick(position: Int, date: LocalDate?)
}