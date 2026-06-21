package com.seunome.bookshelf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seunome.bookshelf.data.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Context

class BookViewModel(private val dao: BookDao) : ViewModel() {

    private val _books = dao.getAllBooks()
    val books: StateFlow<List<Book>> = _books
        .map { entities -> entities.map { it.toBook() } }
        .asStateFlow()

    fun saveBook(book: Book) = viewModelScope.async {
        dao.insertBook(book.toEntity())
    }

    fun deleteBook(book: Book) = viewModelScope.async {
        dao.deleteBook(book.toEntity())
    }

    suspend fun getBookById(id: Int): Book? {
        return withContext(Dispatchers.IO) {
            dao.getBookById(id)?.toBook()
        }
    }
}