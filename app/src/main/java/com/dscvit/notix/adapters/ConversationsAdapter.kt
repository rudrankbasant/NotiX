package com.dscvit.notix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.model.NotificationData

class ConversationsAdapter(
    val context: Context,
    private val conversationsClickInterface: ConversationsClickInterface,
) : RecyclerView.Adapter<ConversationsAdapter.ViewHolder>() {

    val allConversations = ArrayList<NotificationData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.conversationName)
        val desc = itemView.findViewById<TextView>(R.id.conversationDesc)
        //val userIcon = itemView.findViewById<ImageView>(R.id.conversDisplayPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview_conversations, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = allConversations[position].title
        if (allConversations[position].desc?.contains("x.gdscSender.x") == true) {
            val seperatedDesc = allConversations[position].desc?.split("x.gdscSender.x")
            holder.desc.text = seperatedDesc?.get(0)
        } else {
            holder.desc.text = allConversations[position].desc
        }
        holder.itemView.setOnClickListener {
            conversationsClickInterface.onConversationsClick(allConversations[position])
        }


    }

    override fun getItemCount(): Int {
        return allConversations.size
    }

    fun updateList(newList: List<NotificationData>) {
        allConversations.clear()
        allConversations.addAll(newList)
        notifyDataSetChanged()
    }
}

interface ConversationsClickInterface {
    fun onConversationsClick(selectedConversation: NotificationData)
}