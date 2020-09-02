package com.shanemaglangit.sharetask.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shanemaglangit.sharetask.databinding.CardTextItemBinding

class CardTextAdapter(private val cardTextListener: CardTextListener) :
    ListAdapter<Pair<String, String>, CardTextAdapter.ViewHolder>(PairDiffCallback()) {

    public override fun getItem(position: Int): Pair<String, String> {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, cardTextListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Used to update the data under the recycler view
     */
    override fun submitList(list: List<Pair<String, String>>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    /**
     * Views that the recycler view uses to display its item
     */
    class ViewHolder private constructor(val binding: CardTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the items to the view holder
         */
        fun bind(item: Pair<String, String>, cardTextListener: CardTextListener) {
            binding.textTitle.text = item.second
            binding.root.setOnClickListener { cardTextListener.onClick(item) }
        }

        /**
         * Static method that is used as a form of factory method for creating a view holder instance
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardTextItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Can be used by the adapter as a way to handle touch events on the view holders
 */
class CardTextListener(val clickListener: (mapEntry: Pair<String, String>) -> Unit) {
    fun onClick(mapEntry: Pair<String, String>) = clickListener(mapEntry)
}

/**
 * Diffcallback that will be used by the cardtextadapter for its "recycling behavior"
 */
class PairDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean = oldItem.first == newItem.first

    override fun areContentsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean = oldItem == newItem
}