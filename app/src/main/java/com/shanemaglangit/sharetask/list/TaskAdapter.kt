package com.shanemaglangit.sharetask.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shanemaglangit.sharetask.data.Task
import com.shanemaglangit.sharetask.databinding.TaskItemBinding

class TaskAdapter(val taskListener: TaskListener) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    public override fun getItem(position: Int): Task {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, taskListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun submitList(list: List<Task>?, filterButtonId: Int) {
        super.submitList(list?.filter {
            when (filterButtonId) {
                1 -> !it.group
                2 -> it.group
                else -> true
            }
        })
    }

    class ViewHolder private constructor(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, taskListener: TaskListener) {
            binding.textTitle.text = item.title
            binding.textSubject.text = item.subject
            binding.progressTask.progress = item.progress
            binding.root.setOnClickListener { taskListener.onClick(item) }
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

class TaskListener(val clickListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = clickListener(task)
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
}