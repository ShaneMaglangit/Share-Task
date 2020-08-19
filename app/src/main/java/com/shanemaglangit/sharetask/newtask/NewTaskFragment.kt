package com.shanemaglangit.sharetask.newtask

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
import com.shanemaglangit.sharetask.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        this.javaClass == NewTaskFragment::class.java

        viewModel.navigationAction.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.navigationComplete()
            }
        })

        return binding.root
    }

}