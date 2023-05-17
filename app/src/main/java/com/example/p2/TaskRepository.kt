package com.example.p2

import androidx.annotation.WorkerThread
import androidx.constraintlayout.helper.widget.Flow
import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }
}