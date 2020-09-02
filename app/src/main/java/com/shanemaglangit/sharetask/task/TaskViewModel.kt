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
    /**
     * Components for performing assisted inject
     */
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

    /**
     * Live data for updating various UI components based on changes in the values
     */
    val task: MutableLiveData<Task> = repository.getTask(taskId)
    val checkboxList: LiveData<MutableList<Checkbox>> = repository.getCheckbox(taskId)

    private val _uploadFile = MutableLiveData<Boolean>()
    val uploadFile: LiveData<Boolean>
        get() = _uploadFile

    /**
     * Used to update a checkbox using the repository
     */
    fun updateCheckbox(checkbox: Checkbox) {
        repository.updateCheckbox(task.value!!, checkbox)
    }

    /**
     * Notifies the UI to show the modal dialog for adding members
     */
    fun promptMemberModalDialog() {
        navigate(TaskFragmentDirections.actionTaskFragmentToUserDialogFragment(task.value!!))
    }

    /**
     * Notifies the UI to show the checkbox dialog for adding members
     */
    fun promptCheckboxModalDialog() {
        navigate(TaskFragmentDirections.actionTaskFragmentToCheckboxDialogFragment(task.value!!))
    }

    /**
     * Notifies the UI to request a file from the storage of the phone
     */
    fun requestFile() {
        _uploadFile.value = true
    }

    /**
     * Used to upload a new file to Firebase Storage and link it to Firebase Database
     */
    fun uploadFile(fileName: String, uri: Uri) {
        repository.uploadFile(taskId, fileName, uri)
    }

    /**
     * Used to retrieve a file and download it using Android's DownloadManager
     */
    fun downloadFile(fileUid: String, fileName: String) {
        repository.downloadFile(fileUid, fileName)
    }

    /**
     * Used to remove a file from Firebase Storage and Database
     */
    fun removeFile(fileUid: String) {
        repository.removeFile(task.value!!.id, fileUid)
    }

    /**
     * Used to remove a member for the task
     */
    fun removeMember(userId: String) {
        // Cancel the action if the user tries to remove himself
        if (repository.userId == userId) task.notifyObserver()
        // Remove the member with the use of the repository
        else repository.removeMember(task.value!!.id, userId)
    }

    /**
     * Used to remove a checkbox from the task
     */
    fun removeCheckbox(checkbox: Checkbox) {
        repository.removeCheckbox(task.value!!, checkbox)
    }

    /**
     * Cleans the live data if the UI successfully request for a file
     */
    fun completedFileRequest() {
        _uploadFile.value = false
    }

    /**
     * Used to navigate to edit
     */
    fun navigateToEdit() {
        navigate(TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(task.value!!))
    }
}