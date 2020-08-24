package com.shanemaglangit.sharetask.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel @ViewModelInject constructor(private val firebaseAuth: FirebaseAuth) :
    ViewModel() {
    var username = firebaseAuth.currentUser!!.displayName!!
    var email = firebaseAuth.currentUser!!.email

    fun signOut() {
        firebaseAuth.signOut()
    }
}