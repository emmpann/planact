package com.efan.planact.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskDao
import com.efan.planact.data.user.User
import com.efan.planact.data.user.UserDao

@Database(entities = [User::class, Task::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao
}