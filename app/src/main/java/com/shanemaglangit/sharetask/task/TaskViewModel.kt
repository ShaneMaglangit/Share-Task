package com.shanemaglangit.sharetask.task

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shanemaglangit.sharetask.data.Checkbox
import com.shanemaglangit.sharetask.data.Task
import timber.log.Timber

class TaskViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    private val _checkboxList = MutableLiveData<List<Checkbox>>()
    val checkboxList: LiveData<List<Checkbox>>
        get() = _checkboxList

    private var checkboxListener: ListenerRegistration? = null

    fun loadTask(taskId: String) {
        firebaseFirestore.collection("/task")
            .document(taskId)
            .addSnapshotListener addSnapShotListener@{ snapshot, e ->
                if (e != null) {
                    Timber.w("Listening to task document failed")
                    return@addSnapShotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val task = snapshot.toObject(Task::class.java)!!.apply { id = taskId }
                    _task.value = task

                    listenToCheckboxes(task.checkboxes)
                }
            }
    }

    fun addNewCheckbox() {
        val checkbox = Checkbox(details = "This is a sample", dateCreated = Timestamp.now())

        firebaseFirestore.collection("/checkbox")
            .add(checkbox)
            .addOnSuccessListener {
                firebaseFirestore.collection("/task")
                    .document(_task.value!!.id)
                    .update("checkboxes", FieldValue.arrayUnion(it.id))
            }
    }

    private fun listenToCheckboxes(checkboxIdList: List<String>) {
        if (checkboxListener != null) (checkboxListener as ListenerRegistration).remove()

        if (checkboxIdList.isNotEmpty()) {
            checkboxListener = firebaseFirestore.collection("/checkbox")
                .whereIn(FieldPath.documentId(), checkboxIdList)
                .addSnapshotListener addSnapShotListener@{ snapshot, e ->
                    if (e != null) {
                        Timber.w("Listening to task document failed")
                        return@addSnapShotListener
                    }

                    if (snapshot != null) {
                        val tempCheckboxList = mutableListOf<Checkbox>()

                        snapshot.forEach {
                            val checkbox = it.toObject(Checkbox::class.java).apply { id = it.id }
                            tempCheckboxList.add(checkbox)
                        }

                        _checkboxList.value = tempCheckboxList
                    }
                }
        } else {
            _checkboxList.value = mutableListOf()
        }

    }
}