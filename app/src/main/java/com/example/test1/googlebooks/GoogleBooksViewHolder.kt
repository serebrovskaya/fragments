package com.example.test1.googlebooks

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.LibraryItem
import com.example.test1.R
import com.example.test1.databinding.ItemListBinding

class GoogleBooksViewHolder(private val binding: ItemListBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LibraryItem.Book) = with(binding) {
        itemText.text = "${item.name} (ID: ${item.id})"

        println("!!! ${item.thumbnail}")

        Glide.with(itemView.context)
            .load(item.thumbnail.replaceFirst("http://", "https://"))
            .error(R.drawable.book)
            .into(itemAvatar)

    }
}
