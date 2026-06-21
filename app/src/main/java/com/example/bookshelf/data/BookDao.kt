package com.seunome.bookshelf.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: Int): BookEntity?

    @Insert(onConflict = InsertOnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)
}