package com.example.p2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "adres") val adres: String?
)