package com.example.p2

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<Product>>
    //fun getAll(): List<Product>

//    @Query("SELECT * FROM product WHERE id IN (:productIds)")
//    fun getAllByIds(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Product

    @Query("SELECT * FROM product WHERE adres LIKE :adres LIMIT 1")
    fun findByAdres(adres: String): Product

    @Insert
    fun insertAll(vararg products: Product)
    //fun insertAll(products: ArrayList<Product>)

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM product")
    suspend fun deleteAll()
}