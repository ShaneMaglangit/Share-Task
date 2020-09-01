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

        fileAdapter = CardTextAdapter(CardTextListener {
            checkForPermissionBeforeDownload(it.first, it.second)
        })
        binding.recyclerFiles.adapter = fileAdapter
        binding.recyclerFiles.layoutManager = LinearLayoutManager(requireContext())

        memberAdapter = CardTextAdapter(CardTextListener { /* TODO: Something */ })
        binding.recyclerMembers.adapter = memberAdapter
        binding.recyclerMembers.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val checkbox = checkboxAdapter.getItem(viewHolder.adapterPosition)
                viewModel.removeCheckbox(checkbox)
            }
        }).attachToRecyclerView(binding.recyclerCheckbox)

        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val file = fileAdapter.getItem(viewHolder.adapterPosition)
                viewModel.removeFile(file.first)
            }
        }).attachToRecyclerView(binding.recyclerFiles)

        ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val member = memberAdapter.getItem(viewHolder.adapterPosition)
                viewModel.removeMember(member.first)
            }
        }).attachToRecyclerView(binding.recyclerMembers)
    }

    override fun onStart() {
        super.onStart()

        viewModel.task.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                memberAdapter.submitList(it.members.toList())
                fileAdapter.submitList(it.files.toList())
            }
        })

        viewModel.checkboxList.observe(viewLifecycleOwner, Observer {
            if (it != null) checkboxAdapter.submitList(it)
        })

        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })

        viewModel.uploadFile.observe(viewLifecycleOwner, Observer {
            if (it) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                startActivityForResult(intent, FILE_REQUEST_CODE)
                viewModel.completedFileRequest()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Snackbar.make(binding.root, "File is being downloaded", Snackbar.LENGTH_SHORT)
                    .show()
                viewModel.downloadFile(fileUid, fileName)
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}