package com.shanemaglangit.sharetask.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.data.User
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class Repository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseDatabase: FirebaseDatabase,
    firebaseAuth: FirebaseAuth
) {
    private val userId = firebaseAuth.currentUser!!.uid

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>>
        get() = _taskList

    private var taskListenerRegistration: ListenerRegistration? = null

    init {
        firebaseFirestore.collection("/user").document(userId)
            .addSnapshotListener addSnapshotListener@{ snapshot, error ->
                if (error != null) {
                    Timber.w("Listening to task collection failed")
                    return@addSnapshotListener
                } else {
                    val user = snapshot?.toObject(User::class.java)
                    listenToTasks(user)

                    if (user != null)
                    else _taskList.value = listOf()
                }
            }
    }

    private fun listenToTasks(user: User?) {
        taskListenerRegistration?.remove()

        if (user != null && user.taskIdList.isNotEmpty()) {
            taskListenerRegistration = firebaseFirestore.collection("/task")
                .whereIn(FieldPath.documentId(), user.taskIdList)
                .addSnapshotListener addSnapshotListener@{ snapshot, error ->
                    if (error != null) {
                        Timber.w("Listening to task collection failed")
                        return@addSnapshotListener
                    } else {
                        val tempTaskList = mutableListOf<Task>()

                        snapshot?.forEach {
                            tempTaskList.add(it.toObject(Task::class.java).apply { id = it.id })
                        }

                        _taskList.value = listOf()
                    }
                }
        } else {
            _taskList.value = listOf()
        }
    }

//    fun getTaskList() : LiveData<List<Task>>
//    fun addTask()
//    fun getTask(taskId: String) : LiveData<Task>
//    fun getCheckbox(taskId: String) : LiveData<List<Checkbox>>
//    fun addCheckbox(taskId: String, checkbox: Checkbox)
//    fun addFile(taskId: String, file: File)
//    fun updateTask(task: Task, checkboxList: List<Checkbox>)
}