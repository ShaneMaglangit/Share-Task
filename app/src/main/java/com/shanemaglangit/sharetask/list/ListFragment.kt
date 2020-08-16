package com.shanemaglangit.sharetask.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.shanemaglangit.sharetask.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater)
        val taskAdapter = TaskAdapter(TaskListener {
            Timber.d("${it.title} has been clicked")
        })

        binding.recyclerTasks.adapter = taskAdapter
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())

        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewModel.filterList(tab.position)
                }
            }
        })

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            viewModel.filterList(binding.tabGroup.selectedTabPosition)
        })

        viewModel.filteredTaskList.observe(viewLifecycleOwner, Observer {
            taskAdapter.submitList(it)
        })

        return binding.root
    }

}