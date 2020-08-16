package com.shanemaglangit.sharetask.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shanemaglangit.sharetask.data.Task
import timber.log.Timber

class ListViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>>
        get() = _taskList

    private val _filteredTaskList = MutableLiveData<List<Task>>()
    val filteredTaskList: LiveData<List<Task>>
        get() = _filteredTaskList

    init {
        firebaseFirestore.collection("/task")
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
    }

    fun filterList(selectedIndex: Int) {
        _filteredTaskList.value = _taskList.value!!.filter {
            when (selectedIndex) {
                1 -> !it.group
                2 -> it.group
                else -> true
            }
        }
    }
}