package com.shanemaglangit.sharetask.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

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