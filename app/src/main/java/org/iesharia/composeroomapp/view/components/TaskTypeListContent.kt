package org.iesharia.composeroomapp.view.components

import androidx.compose.runtime.Composable
import org.iesharia.composeroomapp.data.entity.TaskType

@Composable
fun TaskTypeListContent(taskTypes: List<TaskType>, onDeleteTaskType: (TaskType) -> Unit) {
    taskTypes.forEach { taskType ->
        TaskTypeItem(taskType = taskType, onDelete = { onDeleteTaskType(taskType) })
    }
}