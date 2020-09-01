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
import androidx.navigation.fragment.navArgs
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import javax.inject.Inject

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    @Inject
    lateinit var viewModelAssistedFactory: NewTaskViewModel.AssistedFactory

    private val args: NewTaskFragmentArgs by navArgs()
    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by viewModels {
        NewTaskViewModel.provideFactory(viewModelAssistedFactory, args.task)
    }

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

        viewModel.navigateUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigateUp()
                viewModel.completedNavigateUp()
            }
        })
    }

    private fun View.addColorPickerDialog() {
        this.setOnClickListener {
            ColorPickerDialog()
                .withPresets(
                    Color.parseColor("#55efc4"),
                    Color.parseColor("#00b894"),
                    Color.parseColor("#81ecec"),
                    Color.parseColor("#00cec9"),
                    Color.parseColor("#74b9ff"),
                    Color.parseColor("#0984e3"),
                    Color.parseColor("#a29bfe"),
                    Color.parseColor("#6c5ce7"),
                    Color.parseColor("#ffeaa7"),
                    Color.parseColor("#fdcb6e"),
                    Color.parseColor("#fab1a0"),
                    Color.parseColor("#e17055"),
                    Color.parseColor("#ff7675"),
                    Color.parseColor("#d63031"),
                    Color.parseColor("#fd79a8"),
                    Color.parseColor("#e84393"),
                    Color.parseColor("#dfe6e9"),
                    Color.parseColor("#2d3436")
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