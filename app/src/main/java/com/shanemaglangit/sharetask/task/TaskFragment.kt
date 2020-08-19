package com.shanemaglangit.sharetask.task

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shanemaglangit.sharecheckbox.checkbox.CheckChangeListener
import com.shanemaglangit.sharecheckbox.checkbox.CheckboxAdapter
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    private val viewModel: TaskViewModel by viewModels()

    private lateinit var checkboxAdapter: CheckboxAdapter
    private lateinit var fileAdapter: CardTextAdapter
    private lateinit var memberAdapter: CardTextAdapter

    private val args: TaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        checkboxAdapter = CheckboxAdapter(CheckChangeListener {
            viewModel.updateCheckbox(it)
        })
        binding.recyclerCheckbox.adapter = checkboxAdapter
        binding.recyclerCheckbox.layoutManager = LinearLayoutManager(requireContext())

        fileAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerFiles.adapter = fileAdapter
        binding.recyclerFiles.layoutManager = LinearLayoutManager(requireContext())

        memberAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerMembers.adapter = memberAdapter
        binding.recyclerMembers.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadTask(args.taskId)

        viewModel.checkboxList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                checkboxAdapter.submitList(it)
            }
        })

        viewModel.task.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                memberAdapter.submitList(it.members.toList())
            }
        })

        viewModel.showCheckboxDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                val editCheckboxText = EditText(requireContext()).apply {
                    inputType = InputType.TYPE_CLASS_TEXT
                    isSingleLine = true
                }

                AlertDialog.Builder(requireContext())
                    .setTitle("Enter the User ID of the new member")
                    .setView(editCheckboxText)
                    .setPositiveButton("Add") { dialog, _ ->
                        viewModel.addNewCheckbox(editCheckboxText.text.toString())
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

                viewModel.checkboxDialogShown()
            }
        })

        viewModel.showMemberDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                val editUserId = EditText(requireContext()).apply {
                    inputType = InputType.TYPE_CLASS_TEXT
                    isSingleLine = true
                }

                AlertDialog.Builder(requireContext())
                    .setTitle("Enter the User ID of the new member")
                    .setView(editUserId)
                    .setPositiveButton("Add") { dialog, _ ->
                        viewModel.addMember(editUserId.text.toString())
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

                viewModel.memberDialogShown()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                AlertDialog.Builder(requireContext())
                    .setTitle("A problem has occurred")
                    .setMessage(it)
                    .setPositiveButton("Ok", null)
                    .show()

                viewModel.errorShown()
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}