package com.efan.planact.ui.fragments.editTask

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskRepository
import com.efan.planact.util.DateUtil
import com.efan.planact.util.StateListener
import kotlinx.coroutines.launch


class EditTaskViewModel @ViewModelInject constructor(
    val taskRepository: TaskRepository
) : ViewModel(), Observable {

    lateinit var task: Task

    var stateListener: StateListener? = null

    @Bindable
    val taskName = MutableLiveData<String>()

    @Bindable
    var dueDate = MutableLiveData<String>()

    @Bindable
    var dueTime = MutableLiveData<String>()

    fun updateTask(view: View) {
        // do some null verification

        if (task != null) {
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
                taskRepository.updateTask(task.copy(
                    name = taskName.value.toString(),
                    dueDate = dueDate.value.toString(),
                    dueTime = dueTime.value.toString()))
            }

            stateListener?.onSuccess("Task was updated.")
            stateListener?.onSuccess(
                task.name,
                DateUtil.getTimeInMillis("${task.dueDate} ${task.dueTime}")
            )
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}