package com.efan.planact.ui.fragments.addTask

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskRepository
import com.efan.planact.data.user.UserRepository
import com.efan.planact.util.DateUtil
import com.efan.planact.util.SharedPreferences
import com.efan.planact.util.StateListener
import kotlinx.coroutines.launch

class CreatetaskViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) :
    ViewModel(), Observable {

    var stateListener: StateListener? = null

    var currentUserId = MutableLiveData<Int>()

    @Bindable
    val taskName = MutableLiveData<String>()

    @Bindable
    var dueDate = MutableLiveData<String>()

    @Bindable
    var dueTime = MutableLiveData<String>()

    fun show(view: View) {
        Log.d("planact", taskName.value.toString())
        Log.d("planact", dueDate.value.toString())
        Log.d("planact", dueTime.value.toString())
        Log.d("planact", currentUserId.toString())
        Log.d("planact", DateUtil.getCurrentDate())
    }

    fun addTask(view: View) {

        if (taskName.value.isNullOrEmpty()) {
            stateListener?.onError("This task is empty.")
            return
        }

        if (dueDate.value.equals("Today")) {
            dueDate.value = DateUtil.getCurrentDate()
        }

        if (dueTime.value.equals("Add time")) {
            stateListener?.onError("Please set the time.")
            return
        }


        viewModelScope.launch {
            try {
                val task = Task(
                    0,
                    taskName.value!!,
                    false,
                    dueDate.value!!,
                    dueTime.value!!,
                    DateUtil.getCurrentDate(),
                    SharedPreferences.getUserId()
                )
                taskRepository.addTask(task)
                stateListener?.onSuccess("Your task was saved.")

                // set notification
                stateListener?.onSuccess(
                    task.name,
                    DateUtil.getTimeInMillis("${task.dueDate} ${task.dueTime}")
                )
            } catch (e: java.lang.Exception) {
                Log.d("error", e.toString())
                stateListener?.onError("This task is empty.")
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}