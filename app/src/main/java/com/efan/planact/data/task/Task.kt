package com.efan.planact.data.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "Name")
    var name: String,

    val completed: Boolean = false,

    @ColumnInfo(name = "DueDate")
    val dueDate: String,

    val dueTime: String,

    val createdAt: String,

    @ColumnInfo(name = "UserId")
    val userId: Int
) : Parcelable