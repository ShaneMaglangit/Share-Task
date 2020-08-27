package com.shanemaglangit.sharetask.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.shanemaglangit.sharetask.model.data.Task
import com.shanemaglangit.sharetask.util.notifyObserver
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Repository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseDatabase: FirebaseDatabase,
    firebaseAuth: FirebaseAuth
) {
    private val userId = firebaseAuth.currentUser!!.uid
    private val dbRef = firebaseDatabase.reference

    private val _taskList = MutableLiveData<MutableList<Task>>(mutableListOf())
    val taskList: LiveData<MutableList<Task>>
        get() = _taskList

    init {
        dbRef.child("/userTask/$userId/")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val task = snapshot.getValue(Task::class.java)

                        if (task != null) {
                            task.id = snapshot.key!!
                            _taskList.value!!.add(task)
                            _taskList.notifyObserver()
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _taskList.value!!.removeAll { it.id == snapshot.key }
                        _taskList.notifyObserver()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val task = snapshot.getValue(Task::class.java)

                        if (task != null) {
                            task.id = snapshot.key!!
                            _taskList.value!!.removeAll { it.id == task.id }
                            _taskList.value!!.add(task)
                            _taskList.notifyObserver()
                        }
                    }
                }
            })
    }

    fun getTask(taskId: String): LiveData<Task> {
        val task = MutableLiveData<Task>()

        dbRef.child("/task/$taskId").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                task.value = snapshot.getValue(Task::class.java)?.apply { id = taskId }
            }
        })

        return task
    }

//    fun getTaskList() : LiveData<List<Task>>
//    fun addTask()
//    fun getTask(taskId: String) : LiveData<Task>
//    fun getCheckbox(taskId: String) : LiveData<List<Checkbox>>
//    fun addCheckbox(taskId: String, checkbox: Checkbox)
//    fun addFile(taskId: String, file: File)
//    fun updateTask(task: Task, checkboxList: List<Checkbox>)
}