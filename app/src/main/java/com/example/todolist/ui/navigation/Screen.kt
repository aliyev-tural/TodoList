package com.example.todolist.ui.navigation

sealed class Screen(val route: String) {
    object TaskListScreen : Screen("task_list_screen")
    object AddEditTaskScreen : Screen("add_edit_task_screen?taskId={taskId}") {
        fun createRoute(taskId: Int? = null) =
            "add_edit_task_screen" + (taskId?.let { "?taskId=$it" } ?: "")
    }
}