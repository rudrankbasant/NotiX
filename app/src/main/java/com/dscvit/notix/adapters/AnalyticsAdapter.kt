package com.dscvit.notix.adapters

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.model.AnalyticsResponse
import java.lang.Exception


class AnalyticsAdapter(
    val context: Context
) : RecyclerView.Adapter<AnalyticsAdapter.ViewHolder>() {

    private var allTopAppsToday = ArrayList<AnalyticsResponse>()
    var parentWidth = 1
    var maxXAxisScale = 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName = itemView.findViewById<TextView>(R.id.appNameAnalytics)
        val numberOfNotifications = itemView.findViewById<TextView>(R.id.analyticsNumber)
        val appIconIV = itemView.findViewById<ImageView>(R.id.appIconAnalytics)
        val backgroundCard = itemView.findViewById<CardView>(R.id.backgroundBar)
        val foregroundCard = itemView.findViewById<CardView>(R.id.foregroundBar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.itemview_analytics, parent, false)
        parentWidth = getPatentWidth(parent)
        return ViewHolder(itemView)
    }

    private fun getPatentWidth(parent: ViewGroup): Int {
        return parent.context.resources.displayMetrics.widthPixels

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val maxDataValue = allTopAppsToday[0].myCount
        maxXAxisScale = (maxDataValue + (5 - (maxDataValue % 5))) * 2
        var pkgArray = allTopAppsToday[position].pkgName.split(".").toTypedArray()
        val pkgName = allTopAppsToday[position].pkgName
        val packageManager = context.packageManager
        holder.appName.text = try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA))
        }catch (e: Exception){
            Log.e("Application Name not found", e.toString())
        }.toString()
        holder.numberOfNotifications.text = allTopAppsToday[position].myCount.toString()
        var foreground = "#FFB200"
        var background = "#FFF5CC"
        when (position % 4) {
            1 -> {
                foreground = "#52FF00"
                background = "#D7FED9"
            }
            2 -> {
                foreground = "#4339F2"
                background = "#DAD7FE"
            }
            3 -> {
                foreground = "#FF3A29"
                background = "#FFE5D3"
            }
            else -> {
                var foreground = "#FFB200"
                var background = "#FFF5CC"
            }
        }
        var layoutParams = holder.foregroundCard.layoutParams
        layoutParams.width = allTopAppsToday[position].myCount * (parentWidth / maxXAxisScale)
        holder.foregroundCard.layoutParams = layoutParams
        //holder.foregroundCard.setCardBackgroundColor(Color.parseColor(foreground))
        //holder.backgroundCard.setCardBackgroundColor(Color.parseColor(background))

        try {
            val drawable: Drawable = context.packageManager
                .getApplicationIcon(allTopAppsToday[position].pkgName)
            holder.appIconIV.setImageDrawable(drawable)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return allTopAppsToday.size
    }

    fun updateList(newList: List<AnalyticsResponse>) {
        allTopAppsToday.clear()
        allTopAppsToday.addAll(newList)
        notifyDataSetChanged()
    }


}