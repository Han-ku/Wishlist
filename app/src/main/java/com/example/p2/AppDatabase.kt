package com.example.p2

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}