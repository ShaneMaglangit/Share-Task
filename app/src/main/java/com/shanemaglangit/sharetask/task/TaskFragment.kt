package com.shanemaglangit.sharetask.task

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentTaskBinding
import com.shanemaglangit.sharetask.util.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TaskFragment : Fragment() {
    companion object {
        const val FILE_REQUEST_CODE = 0
        const val PERMISSION_REQUEST_CODE = 0
    }

    @Inject
    lateinit var viewModelAssistedFactory: TaskViewModel.AssistedFactory

    // View binding for the layout
    private lateinit var binding: FragmentTaskBinding

    // Adapters for the recycler views
    private lateinit var checkboxAdapter: CheckboxAdapter
    private lateinit var fileAdapter: CardTextAdapter
    private lateinit var memberAdapter: CardTextAdapter

    // Navigation arguments passed from the previous fragment
    private val args: TaskFragmentArgs by navArgs()

    // View model for the fragment
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.provideFactory(viewModelAssistedFactory, args.taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set up the data binding and inflate the layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set up the adapter for the checkbox recycler view
        checkboxAdapter = CheckboxAdapter(CheckboxListener { viewModel.updateCheckbox(it) })
        binding.recyclerCheckbox.adapter = checkboxAdapter
        binding.recyclerCheckbox.layoutManager = LinearLayoutManager(requireContext())

        // Set up the adapter for the file recycler view
        fileAdapter = CardTextAdapter(CardTextListener {
            checkForPermissionBeforeDownload(it.first, it.second)
        })
        binding.recyclerFiles.adapter = fileAdapter
        binding.recyclerFiles.layoutManager = LinearLayoutManager(requireContext())

        // Setup the adapter fro the card recycler view
        memberAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerMembers.adapter = memberAdapter
        binding.recyclerMembers.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up the swipe to delete callback for the items within the recycler view
        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the item from the swiped view holder
                val checkbox = checkboxAdapter.getItem(viewHolder.adapterPosition)
                // Pass the item to be deleted from the database
                viewModel.removeCheckbox(checkbox)
            }
        }).attachToRecyclerView(binding.recyclerCheckbox)

        // Set up the swipe to delete callback for the items within the recycler view
        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the item from the swiped view holder
                val file = fileAdapter.getItem(viewHolder.adapterPosition)
                // Pass the item to be deleted from the database
                viewModel.removeFile(file.first)
            }
        }).attachToRecyclerView(binding.recyclerFiles)

        // Set up the swipe to delete callback for the items within the recycler view
        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the item from the swiped view holder
                val member = memberAdapter.getItem(viewHolder.adapterPosition)
                // Pass the item to be deleted from the database
                viewModel.removeMember(member.first)
            }
        }).attachToRecyclerView(binding.recyclerMembers)
    }

    override fun onStart() {
        super.onStart()

        // Observe the live data for the current task and update the list if any change is made
        viewModel.task.observe(viewLifecycleOwner, Observer {
            // Submit the new data if it is not null
            if (it != null) {
                memberAdapter.submitList(it.members.toList())
                fileAdapter.submitList(it.files.toList())
            }
        })

        // Observe the live data for the current list of checkbox and update recycler view accordingly
        viewModel.checkboxList.observe(viewLifecycleOwner, Observer {
            // Submit the new data if it is not null
            if (it != null) checkboxAdapter.submitList(it)
        })

        // Observe the navigation direction live data and navigate accordingly
        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // Navigate based on the given navigation direction
                findNavController().navigate(it)
                // Reset the navigation direction live data
                viewModel.completedNavigation()
            }
        })

        // Observe the upload file live data and request file is necessary
        viewModel.uploadFile.observe(viewLifecycleOwner, Observer {
            if (it) {
                // Create the intent for requesting files to be uploaded
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                startActivityForResult(intent, FILE_REQUEST_CODE)

                // Reset the live data for uploading files
                viewModel.completedFileRequest()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check if the request code matches and if there is a data retrieved
        if (requestCode == FILE_REQUEST_CODE && data != null) {
            try {
                val contentResolver = requireActivity().contentResolver
                val cursor = data.data?.let { contentResolver.query(it, null, null, null, null) }

                if (cursor != null) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()

                    val fileName = cursor.getString(nameIndex)
                    viewModel.uploadFile(fileName, data.data!!)
                }
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }
    }

    private fun checkForPermissionBeforeDownload(fileUid: String, fileName: String) {
        // Checks if the necessary permission is given to the app
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Snackbar.make(binding.root, "File is being downloaded", Snackbar.LENGTH_SHORT)
                    .show()
                // Perform the download of the file is the permission is granted
                viewModel.downloadFile(fileUid, fileName)
            }
            else -> {
                // Request the permission if it is not granted yet
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}