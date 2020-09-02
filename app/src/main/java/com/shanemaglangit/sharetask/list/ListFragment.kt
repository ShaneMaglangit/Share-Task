package com.shanemaglangit.sharetask.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.shanemaglangit.sharetask.databinding.FragmentListBinding
import com.shanemaglangit.sharetask.util.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    // Binding for the list fragment
    private lateinit var binding: FragmentListBinding

    // View model for the list fragment
    private val viewModel: ListViewModel by viewModels()

    // Adapter for the list of task previews
    private lateinit var taskPreviewAdapter: TaskPreviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create binding and inflate the layout
        binding = FragmentListBinding.inflate(inflater)

        // Create the adapter for the recycler view for task preview
        taskPreviewAdapter = TaskPreviewAdapter(TaskPreviewListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToTaskFragment(it.id))
        })

        // Set up the recycler view along with its task adapter
        binding.recyclerTasks.adapter = taskPreviewAdapter
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the swipe to delete callback for the recycler view containg the task previews
        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskPreview = taskPreviewAdapter.getItem(viewHolder.adapterPosition)
                viewModel.removeTask(taskPreview)
            }
        }).attachToRecyclerView(binding.recyclerTasks)

        // A tab listener that updates the list and filters them based on the selected item
        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    taskPreviewAdapter.submitList(viewModel.taskList.value, tab.position)
                }
            }
        })

        // Navigate to the fragment for creating a new task
        binding.fabAdd.setOnClickListener { viewModel.navigateToNewTask() }
    }

    override fun onStart() {
        super.onStart()
        // Observes the task list live data
        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            taskPreviewAdapter.submitList(it, binding.tabGroup.selectedTabPosition)
        })

        // Observe the navigation direction live data and navigate accordingly
        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })
    }
}