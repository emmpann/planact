package com.efan.planact.util

interface StateListener {
    fun onLoading()

    fun onSuccess(message: String?)

    fun onSuccess(message: String?, id: Int){}

    fun onSuccess(message: String?, timeInMillis: Long){}

    fun onError(message: String)
}