package com.example.smartcafe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val userEmail: String,
    val rating: Int,
    val comment: String = ""
)