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
    // View model factory for assisted injection
    @Inject
    lateinit var viewModelAssistedFactory: NewTaskViewModel.AssistedFactory

    // Navigation arguments from the previous fragments
    private val args: NewTaskFragmentArgs by navArgs()

    // Binding for the new task fragment
    private lateinit var binding: FragmentNewTaskBinding

    // View model for the new task fragment
    private val viewModel: NewTaskViewModel by viewModels {
        NewTaskViewModel.provideFactory(viewModelAssistedFactory, args.task)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create the binding and inflate the layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Add a click listener to the color selection components that shows a color picker dialog
        binding.textColor.addColorPickerDialog()
        binding.viewColorPreview.addColorPickerDialog()
    }

    override fun onStart() {
        super.onStart()

        // Listen to the navigateUp live data
        viewModel.navigateUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                // Go back to the previous fragment
                findNavController().navigateUp()
                // Reset the live data
                viewModel.completedNavigateUp()
            }
        })
    }

    /**
     * An extension function for a attaching a click listener that shows a color picker dialog
     */
    private fun View.addColorPickerDialog() {
        this.setOnClickListener {
            // Create the color picker dialog
            ColorPickerDialog()
                // Add color presets
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
                // Remove alpha from the picker
                .withAlphaEnabled(false)
                // Add a lister to the dialog
                .withListener { pickerView, color ->
                    // Updates the selected color
                    viewModel.updateColor(color)
                    // Dismiss the dialog
                    pickerView?.dismiss()
                }
                .show(this@NewTaskFragment.childFragmentManager, "Color Picker Dialog")
        }
    }
}