package com.shanemaglangit.sharetask.util

import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.shanemaglangit.sharetask.R

@BindingAdapter("error")
fun setError(view: TextInputLayout, value: String?) {
    view.error = value
}

@BindingAdapter("disabled")
fun setDisabled(view: View, value: Boolean?) {
    if (value != null) view.isEnabled = !value
}

@BindingAdapter("visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = when (value) {
        true -> View.VISIBLE
        false -> View.GONE
        else -> view.visibility
    }
}

@BindingAdapter("grouped")
fun setGrouped(view: RadioGroup, isGrouped: Boolean) {
    if (view.checkedRadioButtonId == R.id.radio_button_group && isGrouped) return
    if (view.checkedRadioButtonId == R.id.radio_button_personal && !isGrouped) return

    view.check(if (isGrouped) R.id.radio_button_group else R.id.radio_button_personal)
}

@InverseBindingAdapter(attribute = "grouped")
fun getGrouped(view: RadioGroup): Boolean = view.checkedRadioButtonId == R.id.radio_button_group

@BindingAdapter("iconColor")
fun setIconColor(view: View, value: Int) {
    view.setBackgroundColor(value)
}

@BindingAdapter("iconColor")
fun setIconColor(view: TextView, value: Int) {
    view.text = String.format("#%06X", 0xFFFFFF and value)
}

@BindingAdapter("foregroundTint")
fun setForegroundTintColor(view: ImageView, value: Int) {
    view.setColorFilter(value)
}