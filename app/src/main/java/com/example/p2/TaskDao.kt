package com.example.p2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    fun getAllByIds(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE name LIKE :taskName LIMIT 1")
    fun findByName(taskName: String): Task

    @Insert
    fun insertAll(vararg tasks: Task)

    @Insert
    fun insert(vararg task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}