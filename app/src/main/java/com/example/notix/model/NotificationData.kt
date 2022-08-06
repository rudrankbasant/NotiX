package com.example.notix.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notix_table")
data class NotificationData(
    val id: Int,
    val title: String?,
    val desc: String?,
    val postedTime: String,
    val pkgName: String,
    var saved: Boolean
){
    @PrimaryKey(autoGenerate = true) var primaryKey = 0
}
