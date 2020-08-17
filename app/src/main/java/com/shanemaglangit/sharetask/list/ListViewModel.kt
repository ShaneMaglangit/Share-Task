package com.shanemaglangit.sharetask.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shanemaglangit.sharetask.data.Task
import com.shanemaglangit.sharetask.data.User
import timber.log.Timber

class ListViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _taskList = MutableLiveData<List<Task>>(mutableListOf())
    val taskList: LiveData<List<Task>>
        get() = _taskList

    private var taskListener: ListenerRegistration? = null

    init {
        listenToUser()
    }

    private fun listenToUser() {
        firebaseFirestore.collection("/user")
            .document(firebaseAuth.currentUser!!.uid)
            .addSnapshotListener addSnapShotListener@{ snapshot, e ->
                if (e != null) {
                    Timber.w("Listening to user collection failed")
                    return@addSnapShotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    listenToTask(user!!.tasks)
                }
            }
    }

    private fun listenToTask(documentIdList: List<String>) {
        if (taskListener != null) (taskListener as ListenerRegistration).remove()

        if (documentIdList.isNotEmpty()) {
            taskListener = firebaseFirestore.collection("/task")
                .whereIn(FieldPath.documentId(), documentIdList)
                .addSnapshotListener addSnapShotListener@{ snapshot, e ->
                    if (e != null) {
                        Timber.w("Listening to task collection failed")
                        return@addSnapShotListener
                    }

                    if (snapshot != null) {
                        val tempTaskList = mutableListOf<Task>()

                        snapshot.forEach {
                            val task = it.toObject(Task::class.java).apply { id = it.id }
                            tempTaskList.add(task)
                        }

                        _taskList.value = tempTaskList
                    }
                }
        } else {
            _taskList.value = mutableListOf()
        }
    }
}