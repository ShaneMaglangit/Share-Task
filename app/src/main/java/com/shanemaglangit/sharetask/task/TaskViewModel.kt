package com.shanemaglangit.sharetask.task

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.sharetask.model.data.Checkbox
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.repository.Repository
import com.shanemaglangit.sharetask.util.BaseViewModel
import com.shanemaglangit.sharetask.util.notifyObserver
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

    val task: MutableLiveData<Task> = repository.getTask(taskId)
    val checkboxList: LiveData<MutableList<Checkbox>> = repository.getCheckbox(taskId)

    private val _uploadFile = MutableLiveData<Boolean>()
    val uploadFile: LiveData<Boolean>
        get() = _uploadFile

    fun updateCheckbox(checkbox: Checkbox) {
        repository.updateCheckbox(task.value!!, checkbox)
    }

    fun promptMemberModalDialog() {
        navigate(TaskFragmentDirections.actionTaskFragmentToUserDialogFragment(task.value!!))
    }

    fun promptCheckboxModalDialog() {
        navigate(TaskFragmentDirections.actionTaskFragmentToCheckboxDialogFragment(task.value!!))
    }

    fun requestFile() {
        _uploadFile.value = true
    }

    fun uploadFile(fileName: String, uri: Uri) {
        repository.uploadFile(taskId, fileName, uri)
    }

    fun downloadFile(fileUid: String, fileName: String) {
        repository.downloadFile(fileUid, fileName)
    }

    fun removeFile(fileUid: String) {
        repository.removeFile(task.value!!.id, fileUid)
    }

    fun removeMember(userId: String) {
        if (repository.userId == userId) task.notifyObserver()
        else repository.removeMember(task.value!!.id, userId)
    }

    fun removeCheckbox(checkbox: Checkbox) {
        repository.removeCheckbox(task.value!!, checkbox)
    }

    fun completedFileRequest() {
        _uploadFile.value = false
    }

    fun navigateToEdit() {
        navigate(TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(task.value!!))
    }
}