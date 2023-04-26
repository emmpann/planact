package com.efan.planact.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun setLogin(isLogin: Boolean) {
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.commit()
    }

    fun setId(id: Int) {
        editor.putInt("id", id)
        editor.commit()
    }

    fun isLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN, false)
    }

    fun getId() : Int {
        return preferences.getInt("id", 0)
    }

    fun removeData() {
        editor.clear()
        editor.commit()
    }

}