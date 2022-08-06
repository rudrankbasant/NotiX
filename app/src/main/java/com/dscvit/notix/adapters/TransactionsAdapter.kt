package com.dscvit.notix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.model.TransactionData

class TransactionsAdapter(
    val context: Context
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private val allTransactions = ArrayList<TransactionData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById<TextView>(R.id.transactionType)
        val amount = itemView.findViewById<TextView>(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview_transactions, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.type.text = allTransactions[position].type
        holder.amount.text = allTransactions[position].amount
    }

    override fun getItemCount(): Int {
        return allTransactions.size
    }

    fun updateList(newList: List<TransactionData>) {
        allTransactions.clear()
        allTransactions.addAll(newList)
        notifyDataSetChanged()
    }
}