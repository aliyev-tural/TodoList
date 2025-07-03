package com.example.todolist.ui.add_edit_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.usecase.GetTaskByIdUseCase
import com.example.todolist.domain.usecase.UpdateTaskUseCase
import com.example.todolist.ui.common.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val upsertTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var taskTitle by mutableStateOf("")
        private set

    var taskDescription by mutableStateOf("")
        private set

    var isTaskCompleted by mutableStateOf(false)
        private set

    private var currentTaskId: Int? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<String>("taskId")?.toIntOrNull()?.let { taskId ->
            if (taskId != -1) {
                viewModelScope.launch {
                    getTaskByIdUseCase(taskId)?.let { task ->
                        currentTaskId = task.id
                        taskTitle = task.title
                        taskDescription = task.description ?: ""
                        isTaskCompleted = task.isCompleted
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.OnTitleChange -> {
                taskTitle = event.title
            }
            is AddEditTaskEvent.OnDescriptionChange -> {
                taskDescription = event.description
            }
            is AddEditTaskEvent.OnDoneChange -> {
                isTaskCompleted = event.isDone
            }
            AddEditTaskEvent.OnSaveTaskClick -> {
                if (taskTitle.isBlank()) {
                    sendUiEvent(UiEvent.ShowSnackbar(message = "Title cannot be empty"))
                    return
                }
                viewModelScope.launch {
                    upsertTaskUseCase(
                        Task(
                            id = currentTaskId ?: 0,
                            title = taskTitle,
                            description = taskDescription.ifBlank { null },
                            isCompleted = isTaskCompleted,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

sealed class AddEditTaskEvent {
    data class OnTitleChange(val title: String) : AddEditTaskEvent()
    data class OnDescriptionChange(val description: String) : AddEditTaskEvent()
    data class OnDoneChange(val isDone: Boolean) : AddEditTaskEvent()
    object OnSaveTaskClick : AddEditTaskEvent()
}
