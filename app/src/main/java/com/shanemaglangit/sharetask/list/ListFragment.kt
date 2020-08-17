package com.shanemaglangit.sharetask.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)

        taskAdapter = TaskAdapter(TaskListener {
            Timber.d("${it.title} has been clicked")
            findNavController().navigate(ListFragmentDirections.actionListFragmentToTaskFragment(it.id))
        })

        binding.recyclerTasks.adapter = taskAdapter
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            taskAdapter.submitList(it, binding.tabGroup.selectedTabPosition)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    taskAdapter.submitList(viewModel.taskList.value, tab.position)
                }
            }
        })

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_newTaskFragment)
        }
    }
}