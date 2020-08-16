package com.shanemaglangit.sharetask.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shanemaglangit.sharetask.authentication.AuthenticationActivity
import com.shanemaglangit.sharetask.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        binding.textUsername.text = viewModel.username
        binding.textAccountId.text = viewModel.accountId

        binding.buttonSignout.setOnClickListener {
            viewModel.signOut()
            requireActivity().finishAffinity()
            startActivity(Intent(context, AuthenticationActivity::class.java))
        }

        return binding.root
    }

}