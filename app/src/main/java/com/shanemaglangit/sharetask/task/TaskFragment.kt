package com.shanemaglangit.sharetask.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskFragment : Fragment() {
    @Inject
    lateinit var viewModelAssistedFactory: TaskViewModel.AssistedFactory

    private lateinit var binding: FragmentTaskBinding

    private lateinit var checkboxAdapter: CheckboxAdapter
    private lateinit var fileAdapter: CardTextAdapter
    private lateinit var memberAdapter: CardTextAdapter

    private val args: TaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.provideFactory(viewModelAssistedFactory, args.taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        checkboxAdapter = CheckboxAdapter(CheckboxListener { viewModel.updateCheckbox(it) })
        binding.recyclerCheckbox.adapter = checkboxAdapter
        binding.recyclerCheckbox.layoutManager = LinearLayoutManager(requireContext())

        fileAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerFiles.adapter = fileAdapter
        binding.recyclerFiles.layoutManager = LinearLayoutManager(requireContext())

        memberAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerMembers.adapter = memberAdapter
        binding.recyclerMembers.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddMember.setOnClickListener {
            findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToUserDialogFragment(viewModel.task.value!!)
            )
        }

        binding.buttonAddCheckbox.setOnClickListener {
            findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToCheckboxDialogFragment(viewModel.task.value!!)
            )
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.task.observe(viewLifecycleOwner, Observer {
            if (it != null) memberAdapter.submitList(it.members.toList())
        })

        viewModel.checkboxList.observe(viewLifecycleOwner, Observer {
            if (it != null) checkboxAdapter.submitList(it)
        })
    }
}