package com.shanemaglangit.sharetask.signup

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.*
import com.shanemaglangit.sharetask.R

class SignUpViewModel @ViewModelInject constructor(private val firebaseAuth: FirebaseAuth) :
    ViewModel() {
    private val _navigationAction = MutableLiveData<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    private val _signedIn = MutableLiveData<Boolean>()
    val signedIn: LiveData<Boolean>
        get() = _signedIn

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

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun signUp() {
        clearErrors()

        if(checkFieldsComplete()) {
            if (!password.value.equals(confirmPassword.value)) {
                _confirmPasswordError.value = "Password does not match"
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                    .addOnSuccessListener {
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder().apply {
                            displayName = username.value
                        }.build()

                        it.user!!.updateProfile(userProfileChangeRequest)
                        it.user!!.sendEmailVerification()

                        _signedIn.value = true
                    }
                    .addOnFailureListener {
                        when (it) {
                            is FirebaseAuthWeakPasswordException -> _passwordError.value =
                                it.message
                            is FirebaseAuthInvalidCredentialsException -> _emailError.value =
                                it.message
                            is FirebaseAuthUserCollisionException -> _emailError.value = it.message
                        }
                    }
            }
        }
    }

    private fun checkFieldsComplete() : Boolean {
        if(username.value == null) _usernameError.value = "Field cannot be empty"
        if(email.value == null) _emailError.value = "Field cannot be empty"
        if(password.value == null) _passwordError.value = "Field cannot be empty"
        if(confirmPassword.value == null) _confirmPasswordError.value = "Field cannot be empty"

        return username.value != null && email.value != null && password.value != null
    }

    private fun clearErrors() {
        _usernameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    fun signedInComplete() {
        _signedIn.value = null
    }

    fun navigateToSignIn() {
        _navigationAction.value = R.id.action_signUpFragment_to_signInFragment
    }

    fun navigationComplete() {
        _navigationAction.value = null
    }
}