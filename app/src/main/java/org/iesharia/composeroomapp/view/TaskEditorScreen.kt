package org.iesharia.composeroomapp.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.viewmodel.TaskTypeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    task: Task? = null,
    onSaveTask: (Task) -> Unit,
    taskTypeViewModel: TaskTypeViewModel,
    onCancel: () -> Unit
) {
    // Campos de la tarea
    var title by remember { mutableStateOf(task?.title ?: "") }
    var taskTypeId by remember { mutableIntStateOf(task?.taskTypeId ?: 1) }
    var description by remember { mutableStateOf(task?.description ?: "") }

    // Menú desplegable
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Seleccionar tipo") }
    val taskTypes by taskTypeViewModel.taskTypes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (task == null) "Añadir Tarea" else "Editar Tarea") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Menú desplegable
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedOption,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Tarea") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        taskTypes.forEachIndexed { index, taskType ->
                            DropdownMenuItem(
                                text = { Text(taskType.title) },
                                onClick = {
                                    selectedOption = taskType.title
                                    taskTypeId = taskType.id
                                    expanded = false
                                }
                            )
                        }
                        // Opción para añadir más tipos de tareas
                        DropdownMenuItem(
                            text = { Text("Añadir Tipo de Tarea") },
                            onClick = {
                                Log.d("TaskEditorScreen", "Añadir Tipo seleccionado")
                                expanded = false
                            }
                        )
                    }
                }

                // Campo de descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre botones
            ) {
                // Boton de guardar tarea
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank()) {
                            onSaveTask(
                                Task(
                                    id = task?.id ?: 0,
                                    title = title,
                                    taskTypeId = taskTypeId,
                                    description = description
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (task == null) "Guardar Tarea" else "Actualizar Tarea")
                }

                // Boton de cancelar
                Button(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
