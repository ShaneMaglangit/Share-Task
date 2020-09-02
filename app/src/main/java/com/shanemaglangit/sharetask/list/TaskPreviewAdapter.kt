package com.shanemaglangit.sharetask.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shanemaglangit.sharetask.databinding.TaskItemBinding
import com.shanemaglangit.sharetask.model.data.TaskPreview

class TaskPreviewAdapter(private val taskPreviewListener: TaskPreviewListener) :
    ListAdapter<TaskPreview, TaskPreviewAdapter.ViewHolder>(TaskPreviewDiffCallback()) {

    public override fun getItem(position: Int): TaskPreview {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: TaskPreviewAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, taskPreviewListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    /**
     * Used to update the data under the recycler view
     */
    fun submitList(list: List<TaskPreview>?, filterButtonId: Int) {
        super.submitList(list?.filter {
            when (filterButtonId) {
                1 -> !it.isGroup
                2 -> it.isGroup
                else -> true
            }
        })
    }

    /**
     * Views that the recycler view uses to display its item
     */
    class ViewHolder private constructor(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the items to the view holder
         */
        fun bind(item: TaskPreview, taskPreviewListener: TaskPreviewListener) {
            binding.taskPreview = item
            binding.taskPreviewListener = taskPreviewListener
            binding.executePendingBindings()
        }

        /**
         * Static method that is used as a form of factory method for creating a view holder instance
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Can be used by the adapter as a way to handle touch events on the view holders
 */
class TaskPreviewListener(val clickListener: (taskPreview: TaskPreview) -> Unit) {
    fun onClick(taskPreview: TaskPreview) = clickListener(taskPreview)
}

/**
 * Diffcallback that will be used by the task preview adapter for its "recycling behavior"
 */
class TaskPreviewDiffCallback : DiffUtil.ItemCallback<TaskPreview>() {
    override fun areItemsTheSame(oldItem: TaskPreview, newItem: TaskPreview): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskPreview, newItem: TaskPreview): Boolean =
        oldItem == newItem
}