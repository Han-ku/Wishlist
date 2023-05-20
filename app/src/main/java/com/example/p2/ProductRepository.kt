package com.example.p2

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ProductRepository (private val productDao: ProductDao){

    val products: Flow<List<Product>> = productDao.getAll()

    fun insert(product: Product){
        productDao.insert(product)
    }

    fun update(product: Product){
        productDao.update(product)
    }


    fun delete(product: Product){
        productDao.delete(product)
    }

}