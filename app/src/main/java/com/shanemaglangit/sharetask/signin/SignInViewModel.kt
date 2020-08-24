package com.shanemaglangit.sharetask.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.util.BaseViewModel

class SignInViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private val _authenticatingUser = MutableLiveData<Boolean>(false)
    val authenticatingUser: LiveData<Boolean>
        get() = _authenticatingUser

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signIn() {
        _emailError.value = if (email.value.isNullOrEmpty()) "Field cannot be empty" else null
        _passwordError.value = if (password.value.isNullOrEmpty()) "Field cannot be empty" else null

        if (!(email.value.isNullOrEmpty() && password.value.isNullOrEmpty())) {
            _authenticatingUser.value = true

            firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnSuccessListener { navigate(R.id.mainActivity) }
                .addOnFailureListener {
                    when (it) {
                        is FirebaseAuthInvalidUserException -> _emailError.value = it.message
                        is FirebaseAuthInvalidCredentialsException -> _passwordError.value =
                            it.message
                    }
                }
                .addOnCompleteListener { _authenticatingUser.value = false }
        }
    }

    fun navigateToSignUp() {
        navigate(R.id.action_signInFragment_to_signUpFragment)
    }
}