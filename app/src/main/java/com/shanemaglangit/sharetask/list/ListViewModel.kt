package com.shanemaglangit.sharetask.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.shanemaglangit.sharetask.model.data.TaskPreview
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel

class ListViewModel @ViewModelInject constructor(private val repository: Repository) :
    BaseViewModel() {
    // Live data the contains the list of task preview under the current user
    val taskList: LiveData<MutableList<TaskPreview>> = repository.taskList

    /**
     * Invokes the repository method for removing a task
     */
    fun removeTask(taskPreview: TaskPreview) {
        repository.removeTask(taskPreview)
    }

    /**
     * Used to navigate to new task
     */
    fun navigateToNewTask() {
        navigate(ListFragmentDirections.actionListFragmentToNewTaskFragment())
    }
}