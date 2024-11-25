package org.iesharia.composeroomapp.view.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.data.entity.TaskType

@Composable
fun TaskListContent(
    tasks: List<Task>,
    taskTypes: List<TaskType>,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            val taskTypeTitle = taskTypes.find { it.id == task.taskTypeId }?.title ?: "Tipo Desconocido"
            TaskItem(
                task = task,
                taskTypeTitle = taskTypeTitle,
                onEdit = { onEditTask(task) },
                onDelete = { onDeleteTask(task) }
            )
        }
    }
}
