package com.example.p2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "name") val name: String?
)