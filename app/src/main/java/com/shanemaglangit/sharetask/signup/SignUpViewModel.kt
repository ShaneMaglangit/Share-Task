package com.shanemaglangit.sharetask.signup

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.shanemaglangit.sharetask.model.data.User
import com.shanemaglangit.sharetask.util.BaseViewModel

class SignUpViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : BaseViewModel() {
    private val _usernameError = MutableLiveData<String>()
    val usernameError: LiveData<String>
        get() = _usernameError

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private val _confirmPasswordError = MutableLiveData<String>()
    val confirmPasswordError: LiveData<String>
        get() = _confirmPasswordError

    private val _creatingUser = MutableLiveData<Boolean>(false)
    val creatingUser: LiveData<Boolean>
        get() = _creatingUser

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun signUp() {
        if (checkFieldsComplete()) {
            if (password.value.equals(confirmPassword.value)) {
                _creatingUser.value = true

                firebaseAuth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                    .addOnSuccessListener {
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .apply { displayName = username.value }
                            .build()

                        val user = User(username = username.value!!, email = email.value!!)

                        it.user!!.updateProfile(userProfileChangeRequest)
                        it.user!!.sendEmailVerification()

                        writeNewUser(it.user!!.uid, user)
                    }
                    .addOnFailureListener {
                        when (it) {
                            is FirebaseAuthWeakPasswordException -> _passwordError.value =
                                it.message
                            is FirebaseAuthInvalidCredentialsException -> _emailError.value =
                                it.message
                            is FirebaseAuthUserCollisionException -> _emailError.value = it.message
                        }

                        _creatingUser.value = false
                    }
            } else {
                _confirmPasswordError.value = "Password does not match"
            }
        }
    }

    private fun writeNewUser(userUid: String, user: User) {
        firebaseDatabase.reference.child("users").child(userUid).setValue(user)
            .addOnCompleteListener {
                val clearedEmail = email.value!!.replace(Regex("[^A-Za-z0-9 ]"), "")
                firebaseDatabase.reference.child("/emailLookup/$clearedEmail")
                    .setValue(userUid)
                    .addOnSuccessListener {
                        navigate(SignUpFragmentDirections.actionSignUpFragmentToMainActivity())
                        _creatingUser.value = false
                    }
            }
    }

    private fun checkFieldsComplete(): Boolean {
        _usernameError.value = if (username.value.isNullOrEmpty()) "Field cannot be empty" else null
        _emailError.value = if (email.value.isNullOrEmpty()) "Field cannot be empty" else null
        _passwordError.value = if (password.value.isNullOrEmpty()) "Field cannot be empty" else null
        _confirmPasswordError.value =
            if (confirmPassword.value.isNullOrEmpty()) "Field cannot be empty" else null

        return username.value != null && email.value != null && password.value != null
    }

    fun navigateToSignIn() {
        navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
    }
}