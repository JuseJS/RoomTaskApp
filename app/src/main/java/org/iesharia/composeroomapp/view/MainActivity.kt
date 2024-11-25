package org.iesharia.composeroomapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.iesharia.composeroomapp.data.database.AppDatabase
import org.iesharia.composeroomapp.navigation.AppNavigation
import org.iesharia.composeroomapp.ui.theme.RoomTaskAppTheme
import org.iesharia.composeroomapp.viewmodel.TaskViewModel
import org.iesharia.composeroomapp.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoomTaskAppTheme {
                val database = AppDatabase.getDatabase(this)
                val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(database.taskDao()))
                val navController = rememberNavController()

                AppNavigation(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    context = this
                )
            }
        }
    }
}
