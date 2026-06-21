package com.example.bookshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookshelf.data.BookStatus
import com.example.bookshelf.ui.theme.ColorRead
import com.example.bookshelf.ui.theme.ColorReading
import com.example.bookshelf.ui.theme.ColorWantToRead

/**
 * Tela principal que mostra a lista de todos os livros cadastrados
 */
@Composable
fun BookListScreen() {
    // Aqui virá a lista completa, por enquanto usamos apenas o cartão de exemplo
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Exemplo de uso do cartão
        BookPreviewCard(
            title = "O Senhor dos Anéis",
            author = "J.R.R. Tolkien",
            status = BookStatus.READING
        )
    }
}

/**
 * Cartão visual que representa um livro individual na lista
 */
@Composable
fun BookPreviewCard(
    title: String,
    author: String,
    status: BookStatus,
    modifier: Modifier = Modifier
) {
    val color = statusColor(status)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Ícone/indicador colorido do lado esquerdo
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            color.copy(alpha = 0.15f),
                            color.copy(alpha = 0.35f)
                        )
                    )
                )
        )

        // Informações do livro ao lado
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "por $author",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            Text(
                text = status.label,
                fontSize = 12.sp,
                color = color
            )
            )
        }
    }

    /**
     * Função auxiliar: retorna a cor correspondente ao status de leitura
     */
    fun statusColor(status: BookStatus): Color {
        return when (status) {
            BookStatus.READ -> ColorRead
            BookStatus.READING -> ColorReading
            BookStatus.WANT_TO_READ -> ColorWantToRead
        }
    }
