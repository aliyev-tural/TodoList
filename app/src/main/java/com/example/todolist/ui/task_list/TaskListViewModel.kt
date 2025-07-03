package com.example.todolist.ui.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.usecase.DeleteTaskUseCase
import com.example.todolist.domain.usecase.GetTasksUseCase
import com.example.todolist.domain.usecase.UpdateTaskUseCase
import com.example.todolist.ui.common.utils.UiEvent
import com.example.todolist.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine // Import combine
import kotlinx.coroutines.flow.flatMapLatest // Import flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val upsertTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    private val _taskFilter = MutableStateFlow(TaskFilter.ALL)
    val taskFilter: Flow<TaskFilter> = _taskFilter

    val tasks: Flow<List<Task>> = combine(
        getTasksUseCase(),
        _taskFilter
    ) { allTasks, currentFilter ->
        when (currentFilter) {
            TaskFilter.ALL -> allTasks
            TaskFilter.ACTIVE -> allTasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> allTasks.filter { it.isCompleted }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var recentlyDeletedTask: Task? = null

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    recentlyDeletedTask = event.task
                    deleteTaskUseCase(event.task)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Task deleted",
                        action = "Undo"
                    ))
                }
            }
            is TaskListEvent.OnDoneTaskClick -> {
                viewModelScope.launch {
                    upsertTaskUseCase(event.task.copy(isCompleted = event.isDone))
                }
            }
            is TaskListEvent.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.AddEditTaskScreen.createRoute(event.task.id)))
            }
            TaskListEvent.OnAddTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.AddEditTaskScreen.createRoute()))
            }
            TaskListEvent.OnUndoDeleteClick -> {
                recentlyDeletedTask?.let { task ->
                    viewModelScope.launch {
                        upsertTaskUseCase(task)
                    }
                }
                recentlyDeletedTask = null
            }
            is TaskListEvent.OnFilterSelected -> {
                _taskFilter.value = event.filter
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

sealed class TaskListEvent {
    data class OnDeleteTaskClick(val task: Task) : TaskListEvent()
    data class OnDoneTaskClick(val task: Task, val isDone: Boolean) : TaskListEvent()
    data class OnTaskClick(val task: Task) : TaskListEvent()
    object OnAddTaskClick : TaskListEvent()
    object OnUndoDeleteClick : TaskListEvent()
    data class OnFilterSelected(val filter: TaskFilter) : TaskListEvent()
}
