package com.example.todolist.data.repository

import com.example.todolist.data.local.TaskDao
import com.example.todolist.data.local.TaskEntity
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomainTask() }
        }
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)?.toDomainTask()
    }

    override suspend fun upsertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}

fun TaskEntity.toDomainTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        timestamp = this.timestamp
    )
}


fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        timestamp = this.timestamp
    )
}