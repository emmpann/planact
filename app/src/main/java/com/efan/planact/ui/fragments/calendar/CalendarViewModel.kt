package com.efan.planact.ui.fragments.calendar

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.efan.planact.data.task.Task
import com.efan.planact.data.task.TaskRepository
import com.efan.planact.util.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class CalendarViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    val dueDate = MutableStateFlow("")

    private val taskFlow = dueDate.flatMapLatest {
        taskRepository.getTasksByDate(SharedPreferences.getUserId(), it)
    }

    val tasks = taskFlow.asLiveData()

    fun onTaskSelected(task: Task){}

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = isChecked))
        }
    }

}