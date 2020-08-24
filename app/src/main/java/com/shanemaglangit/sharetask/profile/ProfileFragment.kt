package com.shanemaglangit.sharetask.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

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
            viewModel.signOut()
            requireActivity().finishAffinity()
            findNavController().navigate(R.id.authenticationActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.textUsername.text = viewModel.username
        binding.textEmail.text = viewModel.email
    }
}