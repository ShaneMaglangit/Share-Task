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
    /**
     * Components for performing assisted inject
     */
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

    // Live data that contains either a new task or loaded task from the previous fragment
    val task = MutableLiveData<Task>(previousTask ?: Task())

    // Live data for navigating up
    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    /**
     * Used to save task to the repository
     */
    fun saveTask() {
        // If the task doesn't have an id, a new task entity should be created in the database
        if (task.value!!.id.isEmpty()) repository.writeTask(task.value!!)
        // Else save the new details to the existing task
        else repository.updateTask(task.value!!)
        // Navigate up once invoked
        _navigateUp.value = true
    }

    /**
     * Used to reset the value of the navigateUp live data
     */
    fun completedNavigateUp() {
        _navigateUp.value = false
    }

    /**
     * Used to update the icon color of the task
     */
    fun updateColor(color: Int) {
        task.value!!.iconColor = color
        task.notifyObserver()
    }
}