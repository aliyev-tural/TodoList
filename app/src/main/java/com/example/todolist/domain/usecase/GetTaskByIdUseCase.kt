package com.example.todolist.domain.usecase

import com.example.todolist.domain.model.Task
import com.example.todolist.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(taskId: Int): Task? {
        return repository.getTaskById(taskId)
    }
}