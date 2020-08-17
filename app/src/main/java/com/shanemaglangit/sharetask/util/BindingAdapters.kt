package com.shanemaglangit.sharetask.util

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.data.TaskType

@BindingAdapter("error")
fun setError(view: TextInputLayout, value: String?) {
    view.error = value
}

@BindingAdapter("checked")
fun setChecked(view: RadioGroup, value: TaskType?) {
    if ((value == null || value == TaskType.PERSONAL) && view.checkedRadioButtonId != R.id.radio_button_personal) {
        view.check(R.id.radio_button_personal)
    } else if (value == TaskType.GROUP && view.checkedRadioButtonId != R.id.radio_button_group) {
        view.check(R.id.radio_button_group)
    }
}

@InverseBindingAdapter(attribute = "checked")
fun getChecked(view: RadioGroup): TaskType? {
    return when (view.checkedRadioButtonId) {
        R.id.radio_button_personal -> TaskType.PERSONAL
        else -> TaskType.GROUP
    }
}