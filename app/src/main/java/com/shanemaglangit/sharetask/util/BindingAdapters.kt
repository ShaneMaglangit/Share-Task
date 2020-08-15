package com.shanemaglangit.sharetask.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("error")
fun setError(view: TextInputLayout, value: String?) {
    view.error = value
}