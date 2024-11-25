package org.iesharia.composeroomapp.view.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.composeroomapp.data.entity.TaskType

@Composable
fun TaskTypeListContent(
    taskTypes: List<TaskType>,
    onDeleteTaskType: (TaskType) -> Unit,
    onEditTaskType: (TaskType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(taskTypes) { taskType ->
            TaskTypeItem(
                taskType = taskType,
                onEdit = { onEditTaskType(taskType) },
                onDelete = { onDeleteTaskType(taskType) }
            )
        }
    }
}
