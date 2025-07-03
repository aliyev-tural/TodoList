package com.example.todolist.ui.add_edit_task

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.ui.common.utils.UiEvent
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val taskTitle = viewModel.taskTitle
    val taskDescription = viewModel.taskDescription
    val isTaskCompleted = viewModel.isTaskCompleted

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTaskEvent.OnSaveTaskClick)
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Task")
            }
        }
    ) { paddingValues ->
        AddEditTaskContent(
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            isTaskCompleted = isTaskCompleted,
            onEvent = viewModel::onEvent,
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskContent(
    taskTitle: String,
    taskDescription: String,
    isTaskCompleted: Boolean,
    onEvent: (AddEditTaskEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { onEvent(AddEditTaskEvent.OnTitleChange(it)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = taskDescription,
            onValueChange = { onEvent(AddEditTaskEvent.OnDescriptionChange(it)) },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Completed", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isTaskCompleted,
                onCheckedChange = { onEvent(AddEditTaskEvent.OnDoneChange(it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditTaskContentPreview() {
    TodoListTheme {
        AddEditTaskContent(
            taskTitle = "Sample Task Title",
            taskDescription = "This is a sample description for the preview.",
            isTaskCompleted = false,
            onEvent = {}
        )
    }
}