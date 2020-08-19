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

    override fun onBindViewHolder(holder: CardTextAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, cardTextListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>, cardTextListener: CardTextListener) {
            binding.textTitle.text = item.second
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardTextItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CardTextListener(val clickListener: (mapEntry: Pair<String, String>) -> Unit) {
    fun onClick(mapEntry: Pair<String, String>) = clickListener(mapEntry)
}

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