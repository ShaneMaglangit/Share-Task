package com.shanemaglangit.sharetask.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    // Sets the threshold to 70% swipe before performing the deletion.
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.7F
}