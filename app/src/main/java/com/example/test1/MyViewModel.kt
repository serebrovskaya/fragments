package com.example.test1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private val mutSelItem = MutableLiveData<LibraryItem>()

    val selItem: LiveData<LibraryItem>
        get() = mutSelItem

    fun generateItem (item: LibraryItem){
        mutSelItem.value = item
    }

}