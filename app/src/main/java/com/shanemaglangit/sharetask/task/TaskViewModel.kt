package com.shanemaglangit.sharetask.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.sharetask.model.data.Checkbox
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class TaskViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted val taskId: String
) : BaseViewModel() {
    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(taskId: String): TaskViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            taskId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(taskId) as T
            }
        }
    }

    val task: LiveData<Task> = repository.getTask(taskId)
    val checkboxList: LiveData<MutableList<Checkbox>> = repository.getCheckbox(taskId)

    fun updateCheckbox(checkbox: Checkbox) {}
}