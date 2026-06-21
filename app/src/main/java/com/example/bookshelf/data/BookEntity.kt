package com.seunome.bookshelf.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val status: String,  // enum class vira String
    val rating: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        status = BookStatus.entries.firstOrNull { it.name == status } ?: BookStatus.WANT_TO_READ,
        rating = rating,
        notes = notes,
        createdAt = createdAt
    )
}

fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        status = status.name,
        rating = rating,
        notes = notes,
        createdAt = createdAt
    )
}