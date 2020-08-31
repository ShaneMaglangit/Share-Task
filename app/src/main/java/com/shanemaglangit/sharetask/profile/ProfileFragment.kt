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
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignout.setOnClickListener {
            firebaseAuth.signOut()
            requireActivity().finishAffinity()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToAuthenticationActivity()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.textUsername.text = firebaseAuth.currentUser!!.displayName!!
        binding.textEmail.text = firebaseAuth.currentUser!!.email
    }
}