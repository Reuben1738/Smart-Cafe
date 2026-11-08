package com.example.smartcafe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val itemsSummary: String, // e.g., "2x Latte, 1x Cake"
    val totalPrice: Double,
    val status: String = "Pending", // "Pending", "Preparing", "Ready"
    val timestamp: Long = System.currentTimeMillis()
)