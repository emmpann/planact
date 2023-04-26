package com.efan.planact.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "Username")
    val username: String,

    @ColumnInfo(name = "Email")
    val email: String,

    @ColumnInfo(name= "Password")
    val password: String
) {

}