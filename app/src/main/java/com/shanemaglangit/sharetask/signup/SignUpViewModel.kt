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
    // Live data for the different sign up fields
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

    /**
     * Method invoked when the user click the submit button
     */
    fun signUp() {
        // Check if all the fields are filled in before proceeding
        if (checkFieldsComplete()) {
            // Also check if the password and confirm password matches
            if (password.value.equals(confirmPassword.value)) {
                // Set _creatingUser live data to true to notify the view to show a loading bar
                _creatingUser.value = true

                // Create the user account with FirebaseAuth
                firebaseAuth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                    .addOnSuccessListener {
                        // Once successful, create a user profile change request to update the user's
                        // display name
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .apply { displayName = username.value }
                            .build()

                        // Create an instance of the user containing the details
                        // This will be put on the database
                        val user = User(username = username.value!!, email = email.value!!)

                        // Request an email validation
                        it.user!!.updateProfile(userProfileChangeRequest)
                        it.user!!.sendEmailVerification()

                        // Invoke the method for writing the user to the database
                        writeNewUser(it.user!!.uid, user)
                    }
                    .addOnFailureListener {
                        // Check what exception was thrown and set it as a value in one of the error fields
                        when (it) {
                            is FirebaseAuthWeakPasswordException -> _passwordError.value =
                                it.message
                            is FirebaseAuthInvalidCredentialsException -> _emailError.value =
                                it.message
                            is FirebaseAuthUserCollisionException -> _emailError.value = it.message
                        }

                        // Stop the user loading bar
                        _creatingUser.value = false
                    }
            } else {
                // Show an error if the password and confirm password does not match
                _confirmPasswordError.value = "Password does not match"
            }
        }
    }

    /**
     * Method used to write the user details to the database
     */
    private fun writeNewUser(userUid: String, user: User) {
        // Create a datbase reference where the user data will be set
        firebaseDatabase.reference.child("users").child(userUid).setValue(user)
            .addOnCompleteListener {
                // Remove the special character in the email since Firebase RTDB doesn't allow
                // special characters on keys
                val clearedEmail = email.value!!.replace(Regex("[^A-Za-z0-9 ]"), "")

                // Create another reference to be used an email lookup for the user
                firebaseDatabase.reference.child("/emailLookup/$clearedEmail")
                    .setValue(userUid)
                    .addOnSuccessListener {
                        // Navigate to the main activity once complete
                        navigate(SignUpFragmentDirections.actionSignUpFragmentToMainActivity())
                    }
                    .addOnCompleteListener {
                        // Stop the loading once complete
                        _creatingUser.value = false
                    }
            }
    }

    /**
     * Used to check if all of the fields is complete
     */
    private fun checkFieldsComplete(): Boolean {
        _usernameError.value = if (username.value.isNullOrEmpty()) "Field cannot be empty" else null
        _emailError.value = if (email.value.isNullOrEmpty()) "Field cannot be empty" else null
        _passwordError.value = if (password.value.isNullOrEmpty()) "Field cannot be empty" else null
        _confirmPasswordError.value =
            if (confirmPassword.value.isNullOrEmpty()) "Field cannot be empty" else null

        return username.value != null && email.value != null && password.value != null
    }

    /**
     * Used to navigate to the sign in fragment
     */
    fun navigateToSignIn() {
        navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
    }
}