package com.example.notix.adapters

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notix.R
import com.example.notix.model.NotificationData


class HistoryAdapter(
    val context: Context,
    private val upDateNotificationInterface: UpDateNotificationInterface,
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    private var allDayNotifications = ArrayList<NotificationData>()
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val appName = itemView.findViewById<TextView>(R.id.appName)
        val time = itemView.findViewById<TextView>(R.id.notificationTime)
        val title = itemView.findViewById<TextView>(R.id.notificationTitle)
        val description = itemView.findViewById<TextView>(R.id.notificationDesc)
        val saveIcon = itemView.findViewById<ImageView>(R.id.saveIcon)
        val appIcon = itemView.findViewById<ImageView>(R.id.appIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemview_history,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pkgArray = allDayNotifications[position].pkgName.split(".").toTypedArray()
        holder.appName.text = pkgArray[pkgArray.size-1]
        holder.title.text = allDayNotifications[position].title
        holder.description.text = allDayNotifications[position].desc
        holder.time.text = allDayNotifications[position].postedTime.slice(0..4)


        updateSavedIcon(holder,position)
        holder.saveIcon.setOnClickListener {
            val tempNotificationData = allDayNotifications[position]
            tempNotificationData.saved = !tempNotificationData.saved
            upDateNotificationInterface.upDateNotification(tempNotificationData)
            updateSavedIcon(holder,position)
        }


        try {
            val drawable: Drawable = context.packageManager
                .getApplicationIcon(allDayNotifications[position].pkgName)
            holder.appIcon.setImageDrawable(drawable)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return allDayNotifications.size
    }

    fun updateList(newList: List<NotificationData>){
        allDayNotifications.clear()
        allDayNotifications.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateSavedIcon(holder: ViewHolder, position: Int){
        if(allDayNotifications[position].saved){
            holder.saveIcon.setImageResource(R.drawable.ic_star)
        }else{
            holder.saveIcon.setImageResource(R.drawable.ic_unstar)
        }
    }

}

interface  UpDateNotificationInterface{
    fun upDateNotification(notificationData: NotificationData)
}