package com.shanemaglangit.sharetask.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shanemaglangit.sharetask.databinding.CheckboxItemBinding
import com.shanemaglangit.sharetask.model.data.Checkbox

class CheckboxAdapter(private val checkboxListener: CheckboxListener) :
    ListAdapter<Checkbox, CheckboxAdapter.ViewHolder>(CheckboxDiffCallback()) {

    public override fun getItem(position: Int): Checkbox {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, checkboxListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Used to update the data under the recycler view
     */
    override fun submitList(list: MutableList<Checkbox>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    /**
     * Views that the recycler view uses to display its item
     */
    class ViewHolder private constructor(val binding: CheckboxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the items to the view holder
         */
        fun bind(item: Checkbox, checkboxListener: CheckboxListener) {
            binding.checkbox = item
            binding.checkboxListener = checkboxListener
            binding.executePendingBindings()
        }

        /**
         * Static method that is used as a form of factory method for creating a view holder instance
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CheckboxItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Can be used by the adapter as a way to handle touch events on the view holders
 */
class CheckboxListener(val clickListener: (checkbox: Checkbox) -> Unit) {
    fun onClick(checkbox: Checkbox) {
        checkbox.checked = !checkbox.checked
        clickListener(checkbox)
    }
}

/**
 * Diffcallback that will be used by the checkbox adapter for its "recycling behavior"
 */
class CheckboxDiffCallback : DiffUtil.ItemCallback<Checkbox>() {
    override fun areItemsTheSame(oldItem: Checkbox, newItem: Checkbox): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Checkbox, newItem: Checkbox): Boolean =
        oldItem == newItem
}