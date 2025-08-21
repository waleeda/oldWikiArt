package com.example.wikiart.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String?,
    val message: String?,
    val timestamp: Long = System.currentTimeMillis()
)
