package com.example.smartcafe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartcafe.data.model.Order

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders WHERE userEmail = :email ORDER BY timestamp DESC")
    suspend fun getOrdersByUser(email: String): List<Order>
}