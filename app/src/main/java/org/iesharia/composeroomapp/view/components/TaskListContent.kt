package org.iesharia.composeroomapp.view.components

import androidx.compose.runtime.Composable
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.data.entity.TaskType

@Composable
fun TaskListContent(tasks: List<Task>, taskTypes: List<TaskType>, onDeleteTask: (Task) -> Unit) {
    tasks.forEach { task ->
        val taskTypeTitle = taskTypes.find { it.id == task.taskTypeId }?.title ?: "Tipo Desconocido"
        TaskItem(task = task, taskTypeTitle = taskTypeTitle, onDelete = { onDeleteTask(task) })
    }
}