package com.example.test1.googlebooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.LibraryItem
import com.example.test1.LibraryViewModel
import com.example.test1.databinding.ItemListBinding

class GoogleBooksAdapter(
) : RecyclerView.Adapter<GoogleBooksViewHolder>() {

    private var books = emptyList<LibraryItem.Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoogleBooksViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return GoogleBooksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoogleBooksViewHolder, position: Int) {
        val item = books[position]
        holder.bind(item)

        holder.itemView.setOnLongClickListener {

            true
        }



    }

    override fun getItemCount(): Int = books.size

    fun submitList(newBooks: List<LibraryItem.Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}