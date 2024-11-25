package org.iesharia.composeroomapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.data.dao.TaskDao

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskDao.getAllTasks().collectLatest { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            if (task.id == 0) {
                taskDao.insertTask(task)
            } else {
                taskDao.updateTask(task)
            }
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            loadTasks()
        }
    }

    fun loadTaskById(taskId: Int) {
        viewModelScope.launch {
            _currentTask.value = taskDao.getTaskById(taskId).firstOrNull()
        }
    }
}


class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
