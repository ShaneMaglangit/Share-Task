package com.shanemaglangit.sharetask.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.*
import com.shanemaglangit.sharetask.R

class SignInViewModel @ViewModelInject constructor(private val firebaseAuth: FirebaseAuth) :
    ViewModel() {
    private val _navigationAction = MutableLiveData<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    private val _signedIn = MutableLiveData<Boolean>()
    val signedIn: LiveData<Boolean>
        get() = _signedIn

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun submit() {
        clearErrors()

        if(checkFieldsComplete()) {
            firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnSuccessListener {
                    _signedIn.value = true
                }
                .addOnFailureListener {
                    when (it) {
                        is FirebaseAuthInvalidUserException -> _emailError.value =
                            it.message
                        is FirebaseAuthInvalidCredentialsException -> _passwordError.value = it.message
                    }
                }
        }
    }

    private fun checkFieldsComplete() : Boolean {
        if(email.value == null) _emailError.value = "Field cannot be empty"
        if(password.value == null) _passwordError.value = "Field cannot be empty"

        return email.value != null && password.value != null
    }

    private fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
    }

    fun signedInComplete() {
        _signedIn.value = null
    }

    fun navigateToSignUp() {
        _navigationAction.value = R.id.action_signInFragment_to_signUpFragment
    }

    fun navigationComplete() {
        _navigationAction.value = null
    }
}