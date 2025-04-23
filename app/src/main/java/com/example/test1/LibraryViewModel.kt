package com.example.test1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.LibraryItem.Book
import com.example.test1.LibraryItem.Disc
import com.example.test1.LibraryItem.Newspaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    val library = Library()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val selectedItem = MutableLiveData<LibraryItem>()

    val selItem: LiveData<LibraryItem>
        get() = selectedItem

    fun generateItem (item: LibraryItem){
        selectedItem.value = item
    }

    private val items = listOf(
        Book(1867, "Война и мир", true, 5202, "Лев Толстой"),
        Book(1866, "Преступление и наказание", false, 672, "Федор Достоевский"),
        Book(90743, "Маугли", true, 202, "Джозеф Киплинг"),
        Newspaper(303, "Комсомольская правда", true, 123),
        Newspaper(923, "Московский комсомолец", false, 456),
        Newspaper(17245, "Сельская жизнь", true, 794),
        Disc(1975, "Богемская рапсодия", true, "CD"),
        Disc(307, "Дэдпул и Росомаха", true, "DVD"),
        Disc(2001, "Voyage", false, "CD")
    )

    fun loadData() {
        if (library.size == 0) {
            _isLoading.value = true
            viewModelScope.launch {
                items.map { item ->
                    async(Dispatchers.IO) {
                        delay(2000)
                        library.addItem(item)
                    }
                }.awaitAll()
                _isLoading.value = false
            }
        } else {
            _isLoading.value = false
        }
    }
}