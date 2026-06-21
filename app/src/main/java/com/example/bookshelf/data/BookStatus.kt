package com.seunome.bookshelf.data

enum class BookStatus {
    WANT_TO_READ,
    READING,
    READ
}

val BookStatus.label: String = when (this) {
    BookStatus.WANT_TO_READ -> "Quero Ler"
    BookStatus.READING      -> "Lendo"
    BookStatus.READ         -> "Lido"
}