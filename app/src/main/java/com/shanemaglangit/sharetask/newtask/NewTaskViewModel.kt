package com.shanemaglangit.sharetask.newtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.notifyObserver
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class NewTaskViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted previousTask: Task?
) : ViewModel() {
    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(previousTask: Task?): NewTaskViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            previousTask: Task?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(previousTask) as T
            }
        }
    }

    val task = MutableLiveData<Task>(previousTask ?: Task())

    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    fun saveTask() {
        if (task.value!!.id.isEmpty()) repository.writeTask(task.value!!)
        else repository.updateTask(task.value!!)
        _navigateUp.value = true
    }

    fun completedNavigateUp() {
        _navigateUp.value = false
    }

    fun updateColor(color: Int) {
        task.value!!.iconColor = color
        task.notifyObserver()
    }
}