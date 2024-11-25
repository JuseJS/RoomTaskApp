// TaskListView.kt
package org.iesharia.composeroomapp.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.composeroomapp.view.components.TaskListContent
import org.iesharia.composeroomapp.view.components.TaskTypeListContent
import org.iesharia.composeroomapp.viewmodel.TaskTypeViewModel
import org.iesharia.composeroomapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskHome(
    taskViewModel: TaskViewModel,
    taskTypeViewModel: TaskTypeViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToAddTaskType: () -> Unit,
    onNavigateToEditTask: (Int) -> Unit,
    onNavigateToEditTaskType: (Int) -> Unit
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val taskTypes by taskTypeViewModel.taskTypes.collectAsState()

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("Lista de Tareas") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        selectedOption,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedOption == "Lista de Tareas") {
                        onNavigateToAddTask()
                    } else {
                        onNavigateToAddTaskType()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Lista de Tareas") },
                        onClick = {
                            selectedOption = ("Lista de Tareas")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tipos de Tareas") },
                        onClick = {
                            selectedOption = ("Tipos de Tareas")
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedOption == "Lista de Tareas") {
                TaskListContent(
                    tasks = tasks,
                    taskTypes = taskTypes,
                    onDeleteTask = { taskViewModel.deleteTask(it) },
                    onEditTask = { onNavigateToEditTask(it.id) }
                )
            } else {
                TaskTypeListContent(
                    taskTypes = taskTypes,
                    onDeleteTaskType = { taskTypeViewModel.deleteTaskType(it) },
                    onEditTaskType = { onNavigateToEditTaskType(it.id) }
                )
            }
        }
    }
}

