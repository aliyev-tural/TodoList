package com.example.todolist.domain.repository

import com.example.todolist.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTaskById(taskId: Int): Task?

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(task: Task)
}