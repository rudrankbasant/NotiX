package com.example.notix.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notix_table")
data class NotificationData(
    val id: Int,
    val title: String?,
    val text: String?,
    val postedTime: Long,
    val pkgName: String
){
    @PrimaryKey(autoGenerate = true) var primaryKey = 0
}
