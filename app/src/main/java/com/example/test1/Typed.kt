package com.example.test1

import androidx.annotation.DrawableRes

class Library {
    private val items = mutableListOf<LibraryItem>()

    val size: Int
        get() = items.size

    fun addItems(vararg items: LibraryItem) {
        this.items.addAll(items)
    }

    fun addItem(item: LibraryItem) {
        items.add(item)
    }

    operator fun get(position: Int): LibraryItem {
        return items[position]
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
    }

        fun printShortInfo() {
            items.forEachIndexed { index, item ->
                print("!!! ${index + 1}. ")
                item.printShortInfo()
            }
        }
}

abstract class LibraryItem(
    val id: Long,
    val name: String,
    var isAvailable: Boolean,
    @DrawableRes val avatar: Int
) {

    fun printShortInfo() {
        println("!!! $name, Доступность: ${if (isAvailable) "Да" else "Нет"}")
    }

    class Book(
        id: Long,
        name: String,
        isAvailable: Boolean,
        val pageCount: Int,
        val author: String,
        val thumbnail: String = "",
    ) : LibraryItem(id, name, isAvailable, R.drawable.book)

    class Newspaper(
        id: Long,
        name: String,
        isAvailable: Boolean,
        val issueNumber: Int
    ) : LibraryItem(id, name, isAvailable, R.drawable.newspaper)

    class Disc(
        id: Long,
        name: String,
        isAvailable: Boolean,
        private val discType: String
    ) : LibraryItem(id, name, isAvailable, R.drawable.disc)
}


