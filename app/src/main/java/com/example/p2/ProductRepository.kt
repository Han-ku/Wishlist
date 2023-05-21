package com.example.p2

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ProductRepository (private val productDao: ProductDao){

    val products: LiveData<List<Product>> = productDao.getAll()

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun update(product: Product){
        productDao.update(product)
    }


    suspend fun delete(product: Product){
        productDao.delete(product)
    }

}