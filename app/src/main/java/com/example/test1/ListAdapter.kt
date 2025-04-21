package com.example.test1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.databinding.ItemListBinding

class ListAdapter(private var items: Library,
                  private val onItemClick: (LibraryItem) -> Unit)
    : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
        holder.itemView.setOnClickListener{
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size


    fun updateList(newItems: Library) {
        items = newItems
        notifyDataSetChanged()
    }
}