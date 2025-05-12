package com.example.test1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test1.databinding.ActivityGooglebooksBinding
import com.example.test1.googlebooks.GoogleBooksAdapter
import com.example.test1.googlebooks.GoogleBooksViewModel
import com.example.test1.googlebooks.RetrofitHelper.creatRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class GoogleBooksActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGooglebooksBinding.inflate(layoutInflater)
    }
    private lateinit var booksAdapter: GoogleBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            merge(
                binding.authorInput.textChanges(),
                binding.titleInput.textChanges()
            )
                .collect {
                    val hasValidAuthor = binding.authorInput.text.length >= 3
                    val hasValidTitle = binding.titleInput.text.length >= 3
                    binding.buttonSearch.isEnabled = hasValidAuthor || hasValidTitle
                }
        }

        binding.buttonSearch.setOnClickListener {
            val author = binding.authorInput.text.toString()
            val title = binding.titleInput.text.toString()

            booksAdapter = GoogleBooksAdapter()
            binding.recyclerView.adapter = booksAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            val layoutManager = binding.recyclerView.layoutManager

            val api = creatRetrofit()
            val viewModel = GoogleBooksViewModel(api)


            binding.progressBar.visibility = View.VISIBLE
            viewModel.searchBooks(author, title)

            viewModel.books.observe(this) { books ->
                binding.progressBar.visibility = View.GONE
                if (books.isNotEmpty()) {
                    books.forEach { book ->
                        println("!!! Name: ${book.name}, id: ${book.id}")
                    }
                } else {
                    Toast.makeText(this, "Книг не нашлось", Toast.LENGTH_SHORT).show()
                }
                booksAdapter.submitList(books)
            }

            viewModel.error.observe(this) { errorMessage ->
                println("!!! $errorMessage")
                errorMessage?.let {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun TextView.textChanges() = callbackFlow {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                trySend(Unit)
            }
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }
        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(watcher) }
    }
}