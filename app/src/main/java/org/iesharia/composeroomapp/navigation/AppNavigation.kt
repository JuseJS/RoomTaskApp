package org.iesharia.composeroomapp.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.view.TaskEditorScreen
import org.iesharia.composeroomapp.view.TaskList
import org.iesharia.composeroomapp.viewmodel.TaskTypeViewModel
import org.iesharia.composeroomapp.viewmodel.TaskViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskTypeViewModel: TaskTypeViewModel,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.TASKLIST
    ) {
        composable(NavRoutes.TASKLIST) {
            TaskList(
                viewModel = taskViewModel,
                onNavigateToAddTask = { navController.navigate(NavRoutes.ADDTASK) }
            )
        }

        composable(NavRoutes.ADDTASK) {
            TaskEditorScreen(
                onSaveTask = { task ->
                    handleSaveTask(task, taskViewModel, navController)
                },
                onCancel = { navController.popBackStack() },
                taskTypeViewModel = taskTypeViewModel,
                onNavigateToAddTaskType = { navController.navigate(NavRoutes.ADDTASK) }
            )
        }

        composable(NavRoutes.EDITTASK) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()

            if (taskId == null) {
                ShowToastAndNavigateBack(context, "El ID de la tarea no es válido.", navController)
            } else {
                LaunchedEffect(taskId) {
                    taskViewModel.loadTaskById(taskId)
                }

                val task by taskViewModel.currentTask.collectAsState()

                task?.let { nonNullTask ->
                    TaskEditorScreen(
                        task = nonNullTask,
                        onSaveTask = { updatedTask ->
                            handleSaveTask(updatedTask, taskViewModel, navController)
                        },
                        onCancel = { navController.popBackStack() },
                        taskTypeViewModel = taskTypeViewModel,
                        onNavigateToAddTaskType = { navController.navigate(NavRoutes.ADDTASK) }
                    )
                }
            }
        }
    }
}

private fun handleSaveTask(task: Task, taskViewModel: TaskViewModel, navController: NavHostController) {
    taskViewModel.addTask(task)
    navController.popBackStack()
}

@Composable
private fun ShowToastAndNavigateBack(context: Context, message: String, navController: NavHostController) {
    LaunchedEffect(Unit) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        navController.popBackStack(route = NavRoutes.TASKLIST, inclusive = false)
    }
}
