package com.example.todolist.ui.task_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.domain.model.Task // <--- IMPORT Task domain model
import com.example.todolist.ui.common.components.TaskItem
import com.example.todolist.ui.common.utils.UiEvent
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks.collectAsState(initial = emptyList()).value
    val currentFilter = viewModel.taskFilter.collectAsState(initial = TaskFilter.ALL).value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TaskListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TaskListEvent.OnAddTaskClick)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        TaskListContent(
            tasks = tasks,
            currentFilter = currentFilter,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListContent(
    tasks: List<Task>,
    currentFilter: TaskFilter,
    onEvent: (TaskListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SegmentedButton(
                selected = currentFilter == TaskFilter.ALL,
                onClick = { onEvent(TaskListEvent.OnFilterSelected(TaskFilter.ALL)) },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("All")
            }
            SegmentedButton(
                selected = currentFilter == TaskFilter.ACTIVE,
                onClick = { onEvent(TaskListEvent.OnFilterSelected(TaskFilter.ACTIVE)) },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Active")
            }
            SegmentedButton(
                selected = currentFilter == TaskFilter.COMPLETED,
                onClick = { onEvent(TaskListEvent.OnFilterSelected(TaskFilter.COMPLETED)) },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Completed")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (tasks.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tasks yet! Click the '+' button to add one.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListContentPreview() {
    TodoListTheme {
        TaskListContent(
            tasks = listOf(
                Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread", isCompleted = false, timestamp = System.currentTimeMillis()),
                Task(id = 3, title = "Finish report", description = "Due by end of day", isCompleted = false, timestamp = System.currentTimeMillis() + 100000)
            ),
            currentFilter = TaskFilter.ALL,
            onEvent = {}
        )
    }
}
