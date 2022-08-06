package com.dscvit.notix.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transaction_table")
data class TransactionData(
    var type: String,
    var amount: String,
    var date: String,
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0
}
