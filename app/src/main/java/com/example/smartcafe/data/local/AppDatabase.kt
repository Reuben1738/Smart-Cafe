package com.example.smartcafe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartcafe.data.model.MenuItem
import com.example.smartcafe.data.model.Order // Add this import!

@Database(entities = [MenuItem::class, Order::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_cafe_database"
                )
                    .fallbackToDestructiveMigration() // This prevents crashes when we change the DB version
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}