package com.shanemaglangit.sharetask.task.userdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shanemaglangit.sharetask.databinding.FragmentUserDialogBinding
import com.shanemaglangit.sharetask.model.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserDialogFragment : BottomSheetDialogFragment() {
    // Binding for the layout
    private lateinit var binding: FragmentUserDialogBinding

    // Navigation arguments from the previous fragment
    private val args: UserDialogFragmentArgs by navArgs()

    // Repository that holds and does that data processing and tasks
    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create the binding and inflate the layout
        binding = FragmentUserDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add a click listener when the user submits the entered text
        binding.buttonSubmit.setOnClickListener {
            // Remove the special characters on the email
            // This is necessary because Firebase RTDB doesn't allow special characters on keys
            val email = binding.editEmail.text!!.replace(Regex("[^A-Za-z0-9 ]"), "")

            // Add the member to the repository
            repository.addMember(args.task, email)

            // Navigate back to the previous fragment
            findNavController().navigateUp()
        }
    }
}