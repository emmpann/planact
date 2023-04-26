package com.efan.planact.ui.fragments.login

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efan.planact.data.task.TaskRepository
import com.efan.planact.data.user.User
import com.efan.planact.data.user.UserRepository
import com.efan.planact.util.SharedPreferences
import com.efan.planact.util.StateListener
import kotlinx.coroutines.launch

class UserViewModel @ViewModelInject constructor(private val userRepository: UserRepository, private val taskRepository: TaskRepository) :
    ViewModel(), Observable {

    var stateListener: StateListener? = null

    @Bindable
    val email = MutableLiveData<String>()

    @Bindable
    val username = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()

    fun loginUser(view: View) {

        viewModelScope.launch {
            try {
                val loginResponse = userRepository.loginUser(email.value!!, password.value!!)

                loginResponse.collect { user ->
                    if (user == null) {
                        stateListener?.onError("User account not found")
                        return@collect
                    } else {
//                        Log.d("planact", user.username + " " + user.id)

                        // pass current user id
                        SharedPreferences.saveUserId(user.id)

                        stateListener?.onSuccess("Successful login", user.id)
                        return@collect
                    }
                }
            } catch (e: Exception) {
                stateListener?.onError("An error occured")
            }
        }
    }

    fun addUser(view: View) {

        viewModelScope.launch {
            try {
                val user = User(0,
                    username.value!!,
                    email.value!!,
                    password.value!!)
                userRepository.addUser(user)
                stateListener?.onSuccess("Successfully register")
                return@launch
            } catch (e: NullPointerException) {
                stateListener?.onError("Register failed")
                return@launch
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}