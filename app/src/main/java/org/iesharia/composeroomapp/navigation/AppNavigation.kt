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
import androidx.navigation.navArgument
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.data.entity.TaskType
import org.iesharia.composeroomapp.view.screens.TaskEditorScreen
import org.iesharia.composeroomapp.view.screens.TaskHome
import org.iesharia.composeroomapp.view.screens.TaskTypeEditorScreen
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
            TaskHome(
                taskViewModel = taskViewModel,
                taskTypeViewModel = taskTypeViewModel,
                onNavigateToAddTask = { navController.navigate(NavRoutes.ADDTASK) },
                onNavigateToAddTaskType = { navController.navigate(NavRoutes.ADDTASKTYPE) },
                onNavigateToEditTask = { taskId -> navController.navigate(NavRoutes.EDITTASK.replace("{taskId}", taskId.toString())) },
                onNavigateToEditTaskType = { taskTypeId -> navController.navigate(NavRoutes.EDITTASKTYPE.replace("{taskTypeId}", taskTypeId.toString())) }
            )
        }

        composable(NavRoutes.ADDTASK) {
            TaskEditorScreen(
                onSaveTask = { task ->
                    handleSaveTask(task, taskViewModel, navController)
                },
                onCancel = { navController.popBackStack() },
                taskTypeViewModel = taskTypeViewModel,
                onNavigateToAddTaskType = { navController.navigate(NavRoutes.ADDTASKTYPE) }
            )
        }

        composable(
            route = NavRoutes.EDITTASK,
            arguments = listOf(navArgument("taskId") { nullable = true })
        ) { backStackEntry ->
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
                        onNavigateToAddTaskType = { navController.navigate(NavRoutes.ADDTASKTYPE) }
                    )
                }
            }
        }

        composable(NavRoutes.ADDTASKTYPE) {
            TaskTypeEditorScreen(
                onSaveTaskType = { taskType ->
                    handleSaveTaskType(taskType, taskTypeViewModel, navController)
                },
                onCancel = { navController.popBackStack() },
            )
        }

        composable(
            route = NavRoutes.EDITTASKTYPE,
            arguments = listOf(navArgument("taskTypeId") { nullable = true })
        ) { backStackEntry ->
            val taskTypeId = backStackEntry.arguments?.getString("taskTypeId")?.toIntOrNull()

            if (taskTypeId == null) {
                ShowToastAndNavigateBack(context, "El ID del tipo de tarea no es válido.", navController)
            } else {
                LaunchedEffect(taskTypeId) {
                    taskTypeViewModel.loadTaskTypeById(taskTypeId)
                }

                val taskType by taskTypeViewModel.currentTaskType.collectAsState()

                taskType?.let { nonNullTaskType ->
                    TaskTypeEditorScreen(
                        taskType = nonNullTaskType.copy(),
                        onSaveTaskType = { updatedTaskType ->
                            handleSaveTaskType(updatedTaskType, taskTypeViewModel, navController)
                        },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

private fun handleSaveTask(task: Task, taskViewModel: TaskViewModel, navController: NavHostController) {
    taskViewModel.saveTask(task)
    navController.popBackStack()
}

private fun handleSaveTaskType(taskType: TaskType, taskTypeViewModel: TaskTypeViewModel, navController: NavHostController) {
    taskTypeViewModel.saveTaskType(taskType)
    navController.popBackStack()
}

@Composable
private fun ShowToastAndNavigateBack(context: Context, message: String, navController: NavHostController) {
    LaunchedEffect(Unit) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        navController.popBackStack(route = NavRoutes.TASKLIST, inclusive = false)
    }
}