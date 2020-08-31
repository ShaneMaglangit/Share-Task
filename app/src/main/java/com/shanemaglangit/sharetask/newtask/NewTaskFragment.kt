package com.shanemaglangit.sharetask.newtask

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textColor.addColorPickerDialog()
        binding.viewColorPreview.addColorPickerDialog()
    }

    override fun onStart() {
        super.onStart()

        viewModel.navigationDirection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(it)
                viewModel.completedNavigation()
            }
        })
    }

    private fun View.addColorPickerDialog() {
        this.setOnClickListener {
            ColorPickerDialog()
                .withPresets(
                    Color.parseColor("#55efc4"),
                    Color.parseColor("#81ecec"),
                    Color.parseColor("#ffeaa7"),
                    Color.parseColor("#fab1a0")
                )
                .withAlphaEnabled(false)
                .withListener { pickerView, color ->
                    viewModel.updateColor(color)
                    pickerView?.dismiss()
                }
                .show(this@NewTaskFragment.childFragmentManager, "Color Picker Dialog")
        }
    }
}