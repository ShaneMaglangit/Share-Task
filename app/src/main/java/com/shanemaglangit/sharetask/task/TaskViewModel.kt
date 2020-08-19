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
import com.shanemaglangit.sharetask.data.User
import timber.log.Timber
import kotlin.math.roundToInt

class TaskViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    private val _checkboxList = MutableLiveData<MutableList<Checkbox>>()
    val checkboxList: LiveData<MutableList<Checkbox>>
        get() = _checkboxList

    private val _showMemberDialog = MutableLiveData<Boolean>()
    val showMemberDialog: LiveData<Boolean>
        get() = _showMemberDialog

    private val _showCheckboxDialog = MutableLiveData<Boolean>()
    val showCheckboxDialog: LiveData<Boolean>
        get() = _showCheckboxDialog

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

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

    fun updateCheckbox(checkbox: Checkbox) {
        firebaseFirestore.collection("/checkbox")
            .document(checkbox.id)
            .set(checkbox)

        updateProgress()
    }

    private fun updateProgress() {
        val checkedCount = _checkboxList.value!!.filter { it.checked }.size.toFloat()
        val max = _checkboxList.value!!.size.toFloat()
        val progress = ((checkedCount / max) * 100).roundToInt()

        _task.value!!.progress = progress

        firebaseFirestore.collection("/task")
            .document(_task.value!!.id)
            .update("progress", progress)
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

                        if (_checkboxList.value != tempCheckboxList) {
                            _checkboxList.value = tempCheckboxList
                        }
                    }
                }
        } else {
            _checkboxList.value = mutableListOf()
        }
    }

    fun promptMemberDialog() {
        _showMemberDialog.value = true
    }

    fun memberDialogShown() {
        _showMemberDialog.value = false
    }

    fun addMember(userId: String) {
        if (!task.value!!.members.containsKey(userId)) {
            firebaseFirestore.collection("/user")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val newMember = it.toObject(User::class.java)
                        _task.value!!.members[userId] = newMember!!.username

                        firebaseFirestore.collection("/task")
                            .document(_task.value!!.id)
                            .set(_task.value!!)

                        firebaseFirestore.collection("/user")
                            .document(userId)
                            .update("tasks", FieldValue.arrayUnion(_task.value!!.id))
                    } else {
                        _error.value = "User does not exists"
                    }
                }
                .addOnFailureListener {
                    _error.value = it.message
                }
        } else {
            _error.value = "User is already a member"
        }
    }

    fun errorShown() {
        _error.value = null
    }

    fun promptCheckboxDialog() {
        _showCheckboxDialog.value = true
    }

    fun checkboxDialogShown() {
        _showCheckboxDialog.value = false
    }

    fun addNewCheckbox(checkboxText: String) {
        val checkbox = Checkbox(details = checkboxText, dateCreated = Timestamp.now())

        _checkboxList.value!!.add(checkbox)

        firebaseFirestore.collection("/checkbox")
            .add(checkbox)
            .addOnSuccessListener {
                firebaseFirestore.collection("/task")
                    .document(_task.value!!.id)
                    .update("checkboxes", FieldValue.arrayUnion(it.id))
                    .addOnFailureListener {
                        _error.value = it.message
                    }

                _task.value!!.checkboxes.add(it.id)
            }
            .addOnFailureListener {
                _error.value = it.message
            }

        updateProgress()
    }
}