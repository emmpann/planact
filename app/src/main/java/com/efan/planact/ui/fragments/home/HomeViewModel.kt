package com.efan.planact.ui.fragments.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskRepository
import com.efan.planact.util.SharedPreferences
import com.efan.planact.util.StateListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val stateListener : StateListener? = null

    val searchQuery = MutableStateFlow("")

    private val taskSearchFlow = searchQuery.flatMapLatest {
        taskRepository.searchTasks(SharedPreferences.getUserId(), it) //(user id, string, dueDate)
    }

    val tasks = taskRepository.getTasks(SharedPreferences.getUserId()).asLiveData()

    val tasksSearchFlow = taskSearchFlow.asLiveData()

    fun onTaskSelected(task: Task){
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = isChecked))
        }
    }

    fun onTaskSwiped(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
        stateListener?.onSuccess("Task deleting was successful.")
    }
}