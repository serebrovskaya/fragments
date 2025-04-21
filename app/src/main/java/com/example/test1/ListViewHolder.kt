package com.example.test1

import androidx.recyclerview.widget.RecyclerView
import com.example.test1.databinding.ItemListBinding

class ListViewHolder(private val binding: ItemListBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LibraryItem) = with(binding) {
        itemText.text = "${item.name} (ID: ${item.id})"
        itemAvatar.setImageResource(item.avatar)

        updateCardView(item.isAvailable)
    }

    private fun updateCardView(isAvailable: Boolean) {
        with(binding) {
            itemText.alpha = if (isAvailable) 1f else 0.3f
            cardView.cardElevation = if (isAvailable) 10f else 1f
        }
    }
}
