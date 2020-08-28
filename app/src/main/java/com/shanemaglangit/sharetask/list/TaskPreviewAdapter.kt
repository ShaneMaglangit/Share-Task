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

    fun submitList(list: List<TaskPreview>?, filterButtonId: Int) {
        super.submitList(list?.filter {
            when (filterButtonId) {
                1 -> !it.isGroup
                2 -> it.isGroup
                else -> true
            }
        })
    }

    class ViewHolder private constructor(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskPreview, taskPreviewListener: TaskPreviewListener) {
            binding.taskPreview = item
            binding.taskPreviewListener = taskPreviewListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskPreviewListener(val clickListener: (taskPreview: TaskPreview) -> Unit) {
    fun onClick(taskPreview: TaskPreview) = clickListener(taskPreview)
}

class TaskPreviewDiffCallback : DiffUtil.ItemCallback<TaskPreview>() {
    override fun areItemsTheSame(oldItem: TaskPreview, newItem: TaskPreview): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskPreview, newItem: TaskPreview): Boolean =
        oldItem == newItem
}