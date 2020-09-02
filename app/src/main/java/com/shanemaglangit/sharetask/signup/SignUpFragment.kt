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
    // Data binding for the layout
    private lateinit var binding: FragmentSignupBinding

    // View model used by the fragment
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create the binding and inflate the layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the navigation direction live data and navigate accordingly
        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })
    }
}