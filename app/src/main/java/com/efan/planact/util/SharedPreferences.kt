package com.efan.planact.util

object SharedPreferences {

    private var _currentUserId: Int = 0

    fun saveUserId(userId: Int) {
        _currentUserId = userId
    }

    fun getUserId(): Int {
        return _currentUserId
    }
}