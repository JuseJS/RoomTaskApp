package org.iesharia.composeroomapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.iesharia.composeroomapp.data.dao.TaskTypeDao
import org.iesharia.composeroomapp.data.entity.TaskType

class TaskTypeViewModel(private val taskTypeDao: TaskTypeDao) : ViewModel() {
    private val _taskTypes = MutableStateFlow<List<TaskType>>(emptyList())
    val taskTypes: StateFlow<List<TaskType>> = _taskTypes

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskTypeDao.getAllTaskTypes().collectLatest { taskTypeList ->
                _taskTypes.value = taskTypeList
            }
        }
    }

    fun addTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.insertTaskType(taskType)
        }
    }

    fun deleteTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.deleteTaskType(taskType)
        }
    }

    fun updateTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.updateTaskType(taskType)
        }
    }

    fun getTaskTypeById(taskTypeId: Int, callback: (TaskType?) -> Unit) {
        viewModelScope.launch {
            val task = taskTypeDao.getTaskTypeById(taskTypeId).firstOrNull()
            callback(task)
        }
    }
}

class TaskTypeViewModelFactory(private val taskTypeDao: TaskTypeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskTypeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskTypeViewModel(taskTypeDao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}

