package com.shanemaglangit.sharetask.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.shanemaglangit.sharetask.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }


    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.authentication_nav_host)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainActivity) finishAffinity()
        }

        // This opens the main activity if the user is already signed in.
        if (firebaseAuth.currentUser != null) navController.navigate(R.id.mainActivity)
    }
}