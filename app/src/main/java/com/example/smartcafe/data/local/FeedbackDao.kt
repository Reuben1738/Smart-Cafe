package com.example.smartcafe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartcafe.data.model.Feedback

@Dao
interface FeedbackDao {
    @Insert
    suspend fun insertFeedback(feedback: Feedback)

    @Query("SELECT * FROM feedback WHERE orderId = :orderId")
    suspend fun getFeedbackForOrder(orderId: Int): List<Feedback>
}