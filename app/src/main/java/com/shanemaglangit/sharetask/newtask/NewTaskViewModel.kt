package com.shanemaglangit.sharetask.newtask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel
import com.shanemaglangit.sharetask.util.notifyObserver

class NewTaskViewModel @ViewModelInject constructor(
    private val repository: Repository
) : BaseViewModel() {
    val task = MutableLiveData<Task>(Task())

    fun saveTask() {
        repository.writeTask(task.value!!)
        navigate(NewTaskFragmentDirections.actionNewTaskFragmentToListFragment())
    }

    fun updateColor(color: Int) {
        task.value!!.iconColor = color
        task.notifyObserver()
    }
}