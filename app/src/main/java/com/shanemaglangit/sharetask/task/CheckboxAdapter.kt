package com.shanemaglangit.sharetask.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shanemaglangit.sharetask.databinding.CheckboxItemBinding
import com.shanemaglangit.sharetask.model.data.Checkbox


class CheckboxAdapter(private val checkChangeListener: CheckChangeListener) :
    ListAdapter<Checkbox, CheckboxAdapter.ViewHolder>(CheckboxDiffCallback()) {

    public override fun getItem(position: Int): Checkbox {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, checkChangeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CheckboxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Checkbox, checkChangeListener: CheckChangeListener) {
            binding.textDetails.text = item.details
            binding.checkboxChecked.isChecked = item.checked

            binding.root.setOnClickListener {
                binding.checkboxChecked.isChecked = !binding.checkboxChecked.isChecked
            }

            binding.checkboxChecked.setOnCheckedChangeListener { _, isChecked ->
                item.checked = isChecked
                checkChangeListener.checkChangeListener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CheckboxItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CheckChangeListener(val checkChangeListener: (checkbox: Checkbox) -> Unit) {
    fun onClick(checkbox: Checkbox) = checkChangeListener(checkbox)
}

class CheckboxDiffCallback : DiffUtil.ItemCallback<Checkbox>() {
    override fun areItemsTheSame(oldItem: Checkbox, newItem: Checkbox): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Checkbox, newItem: Checkbox): Boolean =
        oldItem == newItem
}