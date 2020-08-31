package com.shanemaglangit.sharetask.model.repository

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.shanemaglangit.sharetask.model.data.Checkbox
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.model.data.TaskPreview
import com.shanemaglangit.sharetask.model.data.User
import com.shanemaglangit.sharetask.util.notifyObserver
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ActivityScoped
class Repository @Inject constructor(
    private val downloadManager: DownloadManager,
    firebaseStorage: FirebaseStorage,
    firebaseDatabase: FirebaseDatabase,
    firebaseAuth: FirebaseAuth
) {
    val userId = firebaseAuth.currentUser!!.uid
    private val userName = firebaseAuth.currentUser!!.displayName!!
    private val dbRef = firebaseDatabase.reference
    private val storageRef = firebaseStorage.reference

    private val _taskList = MutableLiveData<MutableList<TaskPreview>>(mutableListOf())
    val taskList: LiveData<MutableList<TaskPreview>>
        get() = _taskList

    init {
        dbRef.child("/userTask/$userId/")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val taskPreview = snapshot.getValue(TaskPreview::class.java)

                        if (taskPreview != null) {
                            taskPreview.id = snapshot.key!!
                            _taskList.value!!.add(taskPreview)
                            _taskList.value!!.sortByDescending { it.dateUpdated }
                            _taskList.notifyObserver()
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _taskList.value!!.removeAll { it.id == snapshot.key }
                        _taskList.value!!.sortByDescending { it.dateUpdated }
                        _taskList.notifyObserver()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val taskPreview = snapshot.getValue(TaskPreview::class.java)

                        if (taskPreview != null) {
                            taskPreview.id = snapshot.key!!
                            _taskList.value!!.removeAll { it.id == taskPreview.id }
                            _taskList.value!!.add(taskPreview)
                            _taskList.value!!.sortByDescending { it.dateUpdated }
                            _taskList.notifyObserver()
                        }
                    }
                }
            })
    }

    fun writeTask(task: Task) {
        task.apply {
            id = dbRef.child("/task").push().key!!
            members[userId] = userName
        }

        dbRef.child("/task/${task.id}").setValue(task)
        dbRef.child("/userTask/$userId/${task.id}").setValue(TaskPreview.createFromTask(task))
        dbRef.child("/userTaskList/$userId/${task.id}").setValue(true)
    }

    fun getTask(taskId: String): MutableLiveData<Task> {
        val task = MutableLiveData<Task>()

        dbRef.child("/task/$taskId").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                task.value = snapshot.getValue(Task::class.java)?.apply { id = taskId }
            }
        })

        return task
    }

    fun getCheckbox(taskId: String): LiveData<MutableList<Checkbox>> {
        val checkboxList = MutableLiveData<MutableList<Checkbox>>(mutableListOf())

        Timber.i("Creating checkbox")
        dbRef.child("/checkbox/$taskId").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    Timber.i("Checkbox child added")
                    val checkbox = snapshot.getValue(Checkbox::class.java)

                    if (checkbox != null) {
                        Timber.i("Adding checkbox child ${checkboxList.value!!.size}")
                        checkbox.id = snapshot.key!!
                        checkboxList.value!!.add(checkbox)
                        checkboxList.value!!.sortByDescending { it.dateCreated }
                        checkboxList.notifyObserver()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Timber.i("Checkbox child removed")
                    checkboxList.value!!.removeAll { it.id == snapshot.key }
                    checkboxList.value!!.sortByDescending { it.dateCreated }
                    checkboxList.notifyObserver()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    Timber.i("Checkbox child changed")
                    val checkbox = snapshot.getValue(Checkbox::class.java)

                    if (checkbox != null) {
                        checkbox.id = snapshot.key!!
                        checkboxList.value!!.removeAll { it.id == checkbox.id }
                        checkboxList.value!!.add(checkbox)
                        checkboxList.value!!.sortBy { it.dateCreated }
                        checkboxList.notifyObserver()
                    }
                }
            }
        })

        return checkboxList
    }

    fun addMember(task: Task, email: String) {
        dbRef.child("/emailLookup/$email").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val taskPreview = TaskPreview.createFromTask(task)

                    if (user != null && !task.members.containsKey(user.userId)) {
                        dbRef.child("/userTaskList/${user.userId}/${task.id}").setValue(true)
                        dbRef.child("/userTask/${user.userId}").setValue(taskPreview)
                        dbRef.child("/task/${task.id}/members/${user.userId}")
                            .setValue(user.username)
                    }
                }
            }
        )
    }

    fun addCheckbox(task: Task, checkboxText: String) {
        val checkbox = Checkbox(details = checkboxText, checked = false)
        val checkboxId = dbRef.child("/checkbox/${task.id}").push().key
        dbRef.child("/checkbox/${task.id}/$checkboxId").setValue(checkbox)
        updateProgressMax(task, 1)
    }

    private fun updateProgress(task: Task, increment: Int) {
        dbRef.child("/task/${task.id}/progress").setValue(task.progress + increment)

        task.members.forEach {
            dbRef.child("/userTask/${it.key}/${task.id}/progress")
                .setValue(task.progress + increment)
        }
    }

    private fun updateProgressMax(task: Task, increment: Int) {
        dbRef.child("/task/${task.id}/progressMax").setValue(task.progressMax + increment)

        task.members.forEach {
            dbRef.child("/userTask/${it.key}/${task.id}/progressMax")
                .setValue(task.progressMax + increment)
        }
    }

    fun uploadFile(taskId: String, fileName: String, uri: Uri) {
        val fileUid = UUID.randomUUID()

        storageRef.child("/$fileUid").putFile(uri)
            .addOnSuccessListener {
                dbRef.child("/task/$taskId/files/${fileUid}").setValue(fileName)
            }
    }

    fun downloadFile(fileUid: String, fileName: String) {
        storageRef.child("/$fileUid").downloadUrl
            .addOnSuccessListener {
                val request = DownloadManager.Request(it)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                downloadManager.enqueue(request)
            }
    }

    fun removeFile(taskId: String, fileUid: String) {
        storageRef.child("/$fileUid").delete()
        dbRef.child("/task/$taskId/files/$fileUid").removeValue()
    }

    fun removeMember(taskId: String, userId: String) {
        dbRef.child("/task/$taskId/members/$userId").removeValue()
        dbRef.child("/userTask/$userId/$taskId").removeValue()
        dbRef.child("/userTaskList/$userId/$taskId").removeValue()
    }

    fun removeCheckbox(task: Task, checkbox: Checkbox) {
        dbRef.child("/checkbox/${task.id}/${checkbox.id}").removeValue()
        if (checkbox.checked) updateProgress(task, -1)
        updateProgressMax(task, -1)
    }
}