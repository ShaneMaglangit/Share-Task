package com.shanemaglangit.sharetask.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationAction.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })
    }
}