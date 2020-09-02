package com.shanemaglangit.sharetask.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.shanemaglangit.sharetask.util.BaseViewModel

class SignInViewModel @ViewModelInject constructor(private val firebaseAuth: FirebaseAuth) :
    BaseViewModel() {
    // Live data connected to the views with two way data binding
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

    /**
     * Method invoked when the user press the submit button.]
     */
    fun signIn() {
        // Check if both of the field is filled
        _emailError.value = if (email.value.isNullOrEmpty()) "Field cannot be empty" else null
        _passwordError.value = if (password.value.isNullOrEmpty()) "Field cannot be empty" else null

        if (!(email.value.isNullOrEmpty() && password.value.isNullOrEmpty())) {
            // Update the live data to tell the UI to show a loading bar
            _authenticatingUser.value = true

            // Use firebase auth to sign in with the given credentials
            firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnSuccessListener {
                    // Navigate to the main activity
                    navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity())
                }
                .addOnFailureListener {
                    // Check what exception is thrown and update the fields is accordingly
                    when (it) {
                        is FirebaseAuthInvalidUserException -> _emailError.value = it.message
                        is FirebaseAuthInvalidCredentialsException -> _passwordError.value =
                            it.message
                    }
                }
                .addOnCompleteListener {
                    // Stop the loading bar
                    _authenticatingUser.value = false
                }
        }
    }

    /**
     * Used to navigate to sign up
     */
    fun navigateToSignUp() {
        navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
    }
}