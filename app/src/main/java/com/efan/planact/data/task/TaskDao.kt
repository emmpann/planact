package com.efan.planact.data.task

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Query("SELECT * FROM task_table WHERE UserId = :userId")
    fun getTasks(userId: Int): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE UserId = :userId AND Name LIKE '%' || :searchQuery || '%'")
    fun searchTask(userId: Int, searchQuery: String): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE UserId=:userId AND DueDate = :dueDate")
    fun getTasksByDate(userId: Int, dueDate: String): Flow<List<Task>>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}