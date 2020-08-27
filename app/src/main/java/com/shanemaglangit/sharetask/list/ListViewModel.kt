package com.shanemaglangit.sharetask.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel

class ListViewModel @ViewModelInject constructor(
    private val repository: Repository
) : BaseViewModel() {
    val taskList: LiveData<MutableList<Task>> = repository.taskList

    fun navigateToNewTask() {
        navigate(R.id.action_listFragment_to_newTaskFragment)
    }
}