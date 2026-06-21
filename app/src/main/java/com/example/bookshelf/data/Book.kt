package com.seunome.bookshelf.data

data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    val status: BookStatus,
    val rating: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)