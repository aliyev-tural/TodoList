package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.local.AppDatabase
import com.example.todolist.data.local.TaskDao
import com.example.todolist.data.repository.TaskRepositoryImpl
import com.example.todolist.domain.repository.TaskRepository
import com.example.todolist.domain.usecase.DeleteTaskUseCase
import com.example.todolist.domain.usecase.GetTaskByIdUseCase
import com.example.todolist.domain.usecase.GetTasksUseCase
import com.example.todolist.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: TaskRepository): GetTasksUseCase {
        return GetTasksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTaskByIdUseCase(repository: TaskRepository): GetTaskByIdUseCase {
        return GetTaskByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpsertTaskUseCase(repository: TaskRepository): UpdateTaskUseCase {
        return UpdateTaskUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(repository)
    }
}