package com.shanemaglangit.sharetask.signin

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentSigninBinding
import com.shanemaglangit.sharetask.databinding.FragmentSignupBinding
import com.shanemaglangit.sharetask.signup.SignUpViewModel
import com.shanemaglangit.sharetask.util.startMainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationAction.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(it)
                viewModel.navigationComplete()
            }
        })

        viewModel.signedIn.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                requireActivity().startMainActivity(sharedPreferences)
                viewModel.navigationComplete()
            }
        })
    }
}