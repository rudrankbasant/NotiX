package com.dscvit.notix.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.WhiteListData
import java.lang.Exception
import java.time.LocalDate

class WhitelistAdapter(
    val context: Context,
    private val onWhitelistClickInterface: OnWhitelistClickInterface
) : RecyclerView.Adapter<WhitelistAdapter.ViewHolder>() {

    private var allPackages = ArrayList<WhiteListData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName = itemView.findViewById<TextView>(R.id.appNameWhitelist)
        val appIcon = itemView.findViewById<ImageView>(R.id.appIconWhitelist)
        val switchCase = itemView.findViewById<SwitchCompat>(R.id.switchWhitelist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.itemview_whitelist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // var pkgArray = allDayNotifications[position].pkgName.split(".").toTypedArray()
        //holder.appName.text = pkgArray[pkgArray.size - 1]

        val pkgName = allPackages[position].pkgName
        val packageManager = context.packageManager
        holder.appName.text = try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA))
        }catch (e: Exception){
            Log.e("Application Name not found", e.toString())
        }.toString()

        updateWhitelistedIcon(holder, position)
        holder.switchCase.setOnClickListener{view ->
            val switchView = view as SwitchCompat
            val whiteListData = allPackages[position]
            when (switchView.isChecked) {
                true -> {
                    whiteListData.whitelisted= true
                    onWhitelistClickInterface.onWhitelistUpdate(whiteListData)
                }
                false -> {
                    whiteListData.whitelisted=false
                    onWhitelistClickInterface.onWhitelistUpdate(whiteListData)
                }
            }
        }

        try {
            val drawable: Drawable = context.packageManager
                .getApplicationIcon(allPackages[position].pkgName)
            holder.appIcon.setImageDrawable(drawable)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return allPackages.size
    }

    fun updateList(newList: List<WhiteListData>) {
        allPackages.clear()
        allPackages.addAll(newList)
        notifyDataSetChanged()
    }

    private fun updateWhitelistedIcon(holder: ViewHolder, position: Int) {
        holder.switchCase.isChecked = allPackages[position].whitelisted
    }

}

interface OnWhitelistClickInterface {
    fun onWhitelistUpdate(whiteListData: WhiteListData)
}
