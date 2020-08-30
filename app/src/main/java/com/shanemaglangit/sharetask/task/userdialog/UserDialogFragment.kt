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
    private lateinit var binding: FragmentUserDialogBinding
    private val args: UserDialogFragmentArgs by navArgs()

    @Inject
    lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSubmit.setOnClickListener {
            val email = binding.editEmail.text!!.replace(Regex("[^A-Za-z0-9 ]"), "")
            repository.addMember(args.task, email)
            findNavController().navigateUp()
        }
    }
}