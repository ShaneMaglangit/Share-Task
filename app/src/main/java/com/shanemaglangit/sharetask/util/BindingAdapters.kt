package com.shanemaglangit.sharetask.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.shanemaglangit.sharetask.model.data.Task

/**
 * Binding adapter used by text input layout to put an error message to the layout
 */
@BindingAdapter("error")
fun setError(view: TextInputLayout, value: String?) {
    view.error = value
}

/**
 * Binding adapter for toggling the visibility of a view based on the boolean passed
 */
@BindingAdapter("visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = when (value) {
        true -> View.VISIBLE
        false -> View.GONE
        else -> view.visibility
    }
}

/**
 * Binding adapter for setting the background color of a view
 * This is used by the icon for each task
 */
@BindingAdapter("iconColor")
fun setIconColor(view: View, value: Int) {
    view.setBackgroundColor(value)
}

/**
 * Binding adapter for converting a color into a hex string
 * Used to show the current hex of the selected color for the icon
 */
@BindingAdapter("iconColor")
fun setIconColor(view: TextView, value: Int) {
    view.text = String.format("#%06X", 0xFFFFFF and value)
}

/**
 * Binding adapter to set the foreground tint of an image view
 * Used by the view holder for task preview recycler
 */
@BindingAdapter("foregroundTint")
fun setForegroundTintColor(view: ImageView, value: Int) {
    view.setColorFilter(value)
}

/**
 * Binding adapter that hides or show the option to toggle if a task is grouped or personal
 * based on the current state and its members
 */
@BindingAdapter("hasMembers")
fun setHasMembers(view: View, value: Task) {
    if (value.members.size > 1 && value.isGroup) view.visibility = View.GONE
}

/**
 * Binding adapter that hides the list of members if the task is set as personal
 */
@BindingAdapter("isGrouped")
fun setIsGrouped(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}