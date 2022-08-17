package com.dscvit.notix.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "notix_table")
data class NotificationData(
    val id: Int,
    val title: String?,
    val desc: String?,
    val whenValue: Long,
    val postedTime: String,
    val postedDate: String,
    val pkgName: String,
    var saved: Boolean,
    var spamScore: Float
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0
}
