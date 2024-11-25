package org.iesharia.composeroomapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.view.TaskEditorScreen
import org.iesharia.composeroomapp.view.TaskList
import org.iesharia.composeroomapp.viewmodel.TaskViewModel

@Composable
fun AppNavigation(navController: NavHostController, taskViewModel: TaskViewModel,
                  context: android.content.Context
) {
    NavHost(
        navController = navController,
        startDestination = "taskList"
    ) {
        composable("taskList") {
            TaskList(
                viewModel = taskViewModel,
                onNavigateToAddTask = { navController.navigate("editTask") }
            )
        }
        composable("editTask") {
            TaskEditorScreen(
                onSaveTask = { task ->
                    taskViewModel.addTask(task)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("editTask/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()

            if (taskId == null) {
                LaunchedEffect(Unit) {
                    Toast.makeText(
                        context,
                        "El ID de la tarea no es válido.",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.popBackStack(route = "taskList", inclusive = false)
                }
            } else {
                val task = remember { mutableStateOf<Task?>(null) }

                LaunchedEffect(taskId) {
                    taskViewModel.getTaskById(taskId) { retrievedTask ->
                        task.value = retrievedTask
                        if (retrievedTask == null) {
                            Toast.makeText(
                                context,
                                "La tarea no existe.",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack(route = "taskList", inclusive = false)
                        }
                    }
                }

                if (task.value != null) {
                    TaskEditorScreen(
                        task = task.value!!,
                        onSaveTask = { updatedTask ->
                            taskViewModel.updateTask(updatedTask)
                            navController.popBackStack()
                        },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
