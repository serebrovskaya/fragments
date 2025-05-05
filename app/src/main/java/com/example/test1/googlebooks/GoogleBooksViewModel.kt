package com.example.test1.googlebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleBooksViewModel (private val api: BooksApi) : ViewModel() {
    private val _books = MutableStateFlow<List<Items>>(emptyList())
    val books: StateFlow<List<Items>> = _books

    fun searchBooks(author: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                //val query = """inauthor:"$author""""
                val response = api.getBooks(query = author) //, maxResults = 3,
                //println("!!!1")
                //_books.value = response.items ?: emptyList()
            } catch (e: Exception) {
                println("!!!2 ${e}")
                _books.value = emptyList()
            }
        }
        println("!!! ${_books.value}")
    }
}