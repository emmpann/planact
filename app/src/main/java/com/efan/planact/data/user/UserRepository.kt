package com.efan.planact.data.user

import androidx.lifecycle.LiveData
import com.efan.planact.data.AppDatabase
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val appDatabase: AppDatabase
    ) {

    val readAllData: LiveData<List<User>> = appDatabase.userDao().readAllData()


    fun loginUser(email: String, password: String) = appDatabase.userDao().loginUser(email, password)

    suspend fun addUser(user: User) = appDatabase.userDao().addUser(user)
}