package com.efan.planact.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskDao
import com.efan.planact.data.user.User
import com.efan.planact.data.user.UserDao

@Database(entities = [User::class, Task::class], version = 1, exportSchema = false)
abstract class DatabaseUtil : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseUtil? = null

        fun getDatabase(context: Context): DatabaseUtil {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseUtil::class.java,
                    "planact_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}