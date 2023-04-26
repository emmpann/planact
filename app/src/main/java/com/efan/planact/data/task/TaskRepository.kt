package com.efan.planact.data.task

import com.efan.planact.data.AppDatabase
import javax.inject.Inject

class TaskRepository @Inject constructor(private val appDatabase: AppDatabase) {

    fun getTasks(userId: Int) = appDatabase.taskDao().getTasks(userId) // userId, name, dueDate

    fun searchTasks(userId: Int, searchQuery: String) = appDatabase.taskDao().searchTask(userId, searchQuery)

    fun getTasksByDate(userId: Int, dueDate: String) = appDatabase.taskDao().getTasksByDate(userId, dueDate)

    suspend fun addTask(task: Task) = appDatabase.taskDao().addTask(task)

    suspend fun updateTask(task: Task) = appDatabase.taskDao().update(task)

    suspend fun deleteTask(task: Task) = appDatabase.taskDao().delete(task)
}