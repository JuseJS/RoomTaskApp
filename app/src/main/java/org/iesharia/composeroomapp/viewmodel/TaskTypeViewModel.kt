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

    private val _currentTaskType = MutableStateFlow<TaskType?>(null)
    val currentTaskType: StateFlow<TaskType?> = _currentTaskType

    init {
        loadTaskTypes()
    }

    private fun loadTaskTypes() {
        viewModelScope.launch {
            taskTypeDao.getAllTaskTypes().collectLatest { taskTypeList ->
                _taskTypes.value = taskTypeList
            }
        }
    }

    fun addTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.insertTaskType(taskType)
            loadTaskTypes()
        }
    }

    fun deleteTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.deleteTaskType(taskType)
            loadTaskTypes()
        }
    }

    fun updateTaskType(taskType: TaskType) {
        viewModelScope.launch {
            taskTypeDao.updateTaskType(taskType)
            loadTaskTypes()
        }
    }

    fun loadTaskTypeById(taskTypeId: Int) {
        viewModelScope.launch {
            _currentTaskType.value = taskTypeDao.getTaskTypeById(taskTypeId).firstOrNull()
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
