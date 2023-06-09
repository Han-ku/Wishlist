package com.example.p2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product")
data class Product(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "id") var id: Int?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "photo") var photo: ByteArray?
): Serializable