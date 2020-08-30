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
    private lateinit var binding: FragmentCheckboxDialogBinding
    private val args: CheckboxDialogFragmentArgs by navArgs()

    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckboxDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSubmit.setOnClickListener {
            repository.addCheckbox(args.task, binding.editCheckbox.text.toString())
            findNavController().navigateUp()
        }
    }
}