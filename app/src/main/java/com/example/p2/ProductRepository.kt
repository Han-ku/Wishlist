package com.example.p2

import androidx.lifecycle.LiveData

class ProductRepository (private val productDao: ProductDao){

    val products: LiveData<List<Product>> = productDao.getAll()

    fun findById(id: Int): LiveData<Product> {
        return productDao.findById(id)
    }

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