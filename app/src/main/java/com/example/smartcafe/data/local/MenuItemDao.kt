package com.example.smartcafe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartcafe.data.model.MenuItem

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    suspend fun getAllMenuItems(): List<MenuItem> // <--- THIS IS CRUCIAL

    @Insert
    suspend fun insertMenuItem(item: MenuItem)
}