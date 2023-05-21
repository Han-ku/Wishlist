package com.example.p2

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Product

    @Query("SELECT * FROM product WHERE adres LIKE :adres LIMIT 1")
    fun findByAdres(adres: String): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM product WHERE id = :id")
    suspend fun deleteProductById(id: Int)
}