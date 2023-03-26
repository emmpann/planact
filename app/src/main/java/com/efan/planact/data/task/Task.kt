package com.efan.planact.data.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val completed: Boolean = false,
    val dueDate: String,
    val dueTime: String,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: Int

) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateInstance().format(completed)
}