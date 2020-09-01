package com.shanemaglangit.sharetask.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.shanemaglangit.sharetask.model.data.TaskPreview
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel

class ListViewModel @ViewModelInject constructor(private val repository: Repository) :
    BaseViewModel() {
    val taskList: LiveData<MutableList<TaskPreview>> = repository.taskList

    fun removeTask(taskPreview: TaskPreview) {
        repository.removeTask(taskPreview)
    }

    fun navigateToNewTask() {
        navigate(ListFragmentDirections.actionListFragmentToNewTaskFragment())
    }
}