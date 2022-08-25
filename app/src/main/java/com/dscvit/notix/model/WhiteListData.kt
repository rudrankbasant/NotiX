package com.dscvit.notix.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "whitelist_table")
data class WhiteListData(
    val pkgName: String,
    var whitelisted: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0
}
