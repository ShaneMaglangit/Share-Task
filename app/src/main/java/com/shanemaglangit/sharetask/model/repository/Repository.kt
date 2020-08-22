package com.shanemaglangit.sharetask.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shanemaglangit.sharetask.model.data.Task
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Repository @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) {
    private val _task = MutableLiveData<List<Task>>()
    val task: LiveData<List<Task>>
        get() = _task

//    fun getTaskList() : LiveData<List<Task>>
//    fun addTask()
//    fun getTask(taskId: String) : LiveData<Task>
//    fun getCheckbox(taskId: String) : LiveData<List<Checkbox>>
//    fun addCheckbox(taskId: String, checkbox: Checkbox)
//    fun addFile(taskId: String, file: File)
//    fun updateTask(task: Task, checkboxList: List<Checkbox>)
}