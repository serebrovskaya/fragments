package com.example.test1.googlebooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.test1.LibraryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleBooksViewModel(private val api: BooksApi) : ViewModel() {
    private var _books = MutableLiveData<List<LibraryItem.Book>>()
    val books: LiveData<List<LibraryItem.Book>> = _books

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun searchBooks(author: String, title: String) {
        val query: String = if (author.isNotEmpty() && title.isNotEmpty()) {
            "inauthor:\"$author\"+intitle:\"$title\""
        } else if (author.isNotEmpty()) {
            "inauthor:\"$author\""
        } else {
            "intitle:\"$title\""
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getBooks(query = query, maxResults = 3)
                if (response.isSuccessful) {
                    val booksList = response.body()?.items?.map { item ->
                        val industryIdentifier =
                            item.volumeInfo?.industryIdentifiers?.firstOrNull()?.identifier?.toLongOrNull()

                        LibraryItem.Book(
                            id = industryIdentifier ?: 0,
                            name = item.volumeInfo?.title ?: "без названия",
                            author = item.volumeInfo?.authors?.joinToString(separator = ", ")
                                ?: "без авторства",
                            pageCount = item.volumeInfo?.pageCount ?: 0,
                            isAvailable = true,
                            thumbnail = item.volumeInfo?.imageLinks?.thumbnail ?: ""
                        )

                    } ?: emptyList()
                    _books.postValue(booksList)
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Неверный запрос"
                        else -> "Ошибка сервера: ${response.code()}"
                    }
                    _error.postValue(errorMessage)
                }
            } catch (e: Exception) {
                _error.postValue("Нет подключения: ${e.localizedMessage}")
                _books.postValue(emptyList())
            }
        }
    }
}