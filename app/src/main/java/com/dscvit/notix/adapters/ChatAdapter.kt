package com.dscvit.notix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.model.NotificationData

class ChatAdapter(
    val context: Context
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    val allChats = ArrayList<NotificationData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender = itemView.findViewById<TextView>(R.id.sender)
        val message = itemView.findViewById<TextView>(R.id.message)
        val time = itemView.findViewById<TextView>(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_msg, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = allChats[position].desc
        holder.time.text = allChats[position].postedTime.slice(0..4)
        if (text?.contains("x.gdscSender.x") == true) {
            val array = text.split("x.gdscSender.x")
            holder.sender.text = array[1]
            holder.message.text = array[0]
        } else {
            holder.sender.text = " "
            holder.message.text = allChats[position].desc
        }
    }

    override fun getItemCount(): Int {
        return allChats.size
    }

    fun updateList(newList: List<NotificationData>) {
        allChats.clear()
        allChats.addAll(newList)
        notifyDataSetChanged()
    }
}