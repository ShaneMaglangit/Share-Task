package com.shanemaglangit.sharetask.task.checkboxdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shanemaglangit.sharetask.databinding.FragmentCheckboxDialogBinding
import com.shanemaglangit.sharetask.model.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckboxDialogFragment : BottomSheetDialogFragment() {
    // Binding for the layout
    private lateinit var binding: FragmentCheckboxDialogBinding

    // Navigation arguments from the previous fragment
    private val args: CheckboxDialogFragmentArgs by navArgs()

    // Repository that holds and does that data processing and tasks
    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create the binding and inflate the layout
        binding = FragmentCheckboxDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add a click listener when the user submits the entered text
        binding.buttonSubmit.setOnClickListener {
            // Add the checkbox to the repository
            repository.addCheckbox(args.task, binding.editCheckbox.text.toString())

            // Navigate back to the previous fragment
            findNavController().navigateUp()
        }
    }
}