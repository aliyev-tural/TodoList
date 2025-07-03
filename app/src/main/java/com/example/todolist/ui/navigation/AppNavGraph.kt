package com.example.todolist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.ui.add_edit_task.AddEditTaskScreen
import com.example.todolist.ui.task_list.TaskListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskListScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.TaskListScreen.route) {
            TaskListScreen(
                onNavigate = { event -> navController.navigate(event.route) }
            )
        }

        composable(
            route = Screen.AddEditTaskScreen.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()

            AddEditTaskScreen(
                onPopBackStack = { navController.popBackStack() },
            )
        }
    }
}