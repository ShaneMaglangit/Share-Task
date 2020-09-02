package com.shanemaglangit.sharetask.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.shanemaglangit.sharetask.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    // Binding used by the fragment
    private lateinit var binding: FragmentProfileBinding

    // FirebaseAuth instance injected by Hilt
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create binding and inflate the layout
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set a click listener on the signout button
        binding.buttonSignout.setOnClickListener {
            // Sign out the current user on firebase auth
            firebaseAuth.signOut()
            // Destroy all of the current activity in the backstack
            requireActivity().finishAffinity()
            // Start the authentication activity
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToAuthenticationActivity()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        // Update the view to the user details
        binding.textUsername.text = firebaseAuth.currentUser!!.displayName!!
        binding.textEmail.text = firebaseAuth.currentUser!!.email
    }
}