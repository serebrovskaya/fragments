package com.example.test1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.databinding.ActivityGooglebooksBinding
import com.example.test1.googlebooks.GoogleBooksViewModel
import com.example.test1.googlebooks.RetrofitHelper.creatRetrofit

class GoogleBooksActivity: AppCompatActivity()  {

    private val binding by lazy {
        ActivityGooglebooksBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonSearch.setOnClickListener {
            val author = binding.textInput.text.toString()
            if (author.isNotBlank()) {
                Toast.makeText(this, "Вы ввели: $author", Toast.LENGTH_SHORT).show()

                val api = creatRetrofit()
                val viewModel = GoogleBooksViewModel(api)
                if (author.isNotEmpty()) {
                    viewModel.searchBooks(author)
                }
            } else {
                Toast.makeText(this, "Пожалуйста, введите текст", Toast.LENGTH_SHORT).show()
            }
        }
    }
}