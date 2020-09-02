package com.shanemaglangit.sharetask.authentication

import android.os.Bundle
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.authentication_nav_host)

        // If the nav destination is pointed towards the main activity, always finish the current
        // activity first to not allow navigating up once logged in.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainActivity) finishAffinity()
        }

        // This opens the main activity if the user is already signed in.
        if (firebaseAuth.currentUser != null) navController.navigate(R.id.mainActivity)
    }
}