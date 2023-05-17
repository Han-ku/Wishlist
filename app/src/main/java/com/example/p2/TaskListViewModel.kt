package com.example.p2

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TaskListViewModel(private val repository: TaskRepository): ViewModel() {
    val allTasks: LiveData<List<Task>> = repository.allTasks

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}