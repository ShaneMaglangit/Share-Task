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
import com.shanemaglangit.sharetask.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    private lateinit var taskPreviewAdapter: TaskPreviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)

        taskPreviewAdapter = TaskPreviewAdapter(TaskPreviewListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToTaskFragment(it.id))
        })

        binding.recyclerTasks.adapter = taskPreviewAdapter
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    taskPreviewAdapter.submitList(viewModel.taskList.value, tab.position)
                }
            }
        })

        binding.fabAdd.setOnClickListener { viewModel.navigateToNewTask() }
    }

    override fun onStart() {
        super.onStart()
        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            taskPreviewAdapter.submitList(it, binding.tabGroup.selectedTabPosition)
        })

        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })
    }
}