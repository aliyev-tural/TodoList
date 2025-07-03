package com.example.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.local.TaskDao
import com.example.todolist.data.local.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}