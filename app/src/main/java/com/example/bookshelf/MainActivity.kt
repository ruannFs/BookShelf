package com.seunome.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seunome.bookshelf.ui.BookListScreen
import com.seunome.bookshelf.ui.BookFormScreen
import com.seunome.bookshelf.ui.BookViewModel
import com.seunome.bookshelf.ui.theme.BookShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = BookViewModel()

        setContent {
            BookShelfTheme {
                BookShelfApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfApp(viewModel: BookViewModel) {
    var currentScreen by remember { mutableStateOf("list") }
    var selectedBookId by remember { mutableStateOf(0) }

    when (currentScreen) {
        "list" -> BookListScreen(
            viewModel = viewModel,
            onAddBook = {
                selectedBookId = 0
                currentScreen = "form"
            },
            onEditBook = { id ->
                selectedBookId = id
                currentScreen = "form"
            }
        )
        "form" -> BookFormScreen(
            viewModel = viewModel,
            bookId = selectedBookId,
            onNavigateBack = { currentScreen = "list" }
        )
    }
}