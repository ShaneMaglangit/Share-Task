package com.shanemaglangit.sharetask.newtask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.data.TaskType

class NewTaskViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _navigationAction = MutableLiveData<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    val title = MutableLiveData<String>()
    val subject = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val type = MutableLiveData<TaskType>()

    fun saveTask() {
        val uid = firebaseAuth.currentUser!!.uid
        val userName = firebaseAuth.currentUser!!.displayName ?: "Unresolved"
        val task = Task(
            title = title.value!!,
            subject = subject.value!!,
            description = description.value!!,
            group = type.value == TaskType.GROUP
        ).apply { members[uid] = userName }

        firebaseFirestore.collection("/task")
            .add(task)
            .addOnSuccessListener { taskDocReference ->
                firebaseFirestore.collection("/user")
                    .document(firebaseAuth.currentUser!!.uid)
                    .update("tasks", FieldValue.arrayUnion(taskDocReference.id))

                _navigationAction.value = R.id.action_newTaskFragment_to_listFragment
            }

    }

    fun navigationComplete() {
        _navigationAction.value = null
    }
}