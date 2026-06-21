package com.seunome.bookshelf.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seunome.bookshelf.data.Book
import com.seunome.bookshelf.data.BookStatus
import com.seunome.bookshelf.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    viewModel: BookViewModel,
    bookId: Int,
    onNavigateBack: () -> Unit
) {
    val isEditing = bookId != 0

    var title       by remember { mutableStateOf("") }
    var author      by remember { mutableStateOf("") }
    var status      by remember { mutableStateOf(BookStatus.WANT_TO_READ) }
    var rating      by remember { mutableIntStateOf(0) }
    var notes       by remember { mutableStateOf("") }
    var existingBook by remember { mutableStateOf<Book?>(null) }

    var titleError  by remember { mutableStateOf(false) }
    var authorError by remember { mutableStateOf(false) }
    var isSaving    by remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        if (isEditing) {
            viewModel.getBookById(bookId)?.let {
                existingBook = it
                title  = it.title
                author = it.author
                status = it.status
                rating = it.rating
                notes  = it.notes
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Cabeçalho com gradiente ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(IndigoDeep, IndigoPrimary))
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            if (isEditing) "Editar Livro" else "Novo Livro",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (isEditing) "Atualize as informações" else "Adicione à sua coleção",
                            color = Color.White.copy(0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ── Formulário com scroll ──
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Preview da capa
                BookPreviewCard(title = title, author = author, status = status)

                // Campo Título
                FormField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    label = "Título do Livro",
                    placeholder = "Ex: Dom Quixote",
                    icon = Icons.Default.Book,
                    isError = titleError,
                    errorMessage = "Por favor, informe o título"
                )

                // Campo Autor
                FormField(
                    value = author,
                    onValueChange = { author = it; authorError = false },
                    label = "Autor",
                    placeholder = "Ex: Miguel de Cervantes",
                    icon = Icons.Default.Person,
                    isError = authorError,
                    errorMessage = "Por favor, informe o autor"
                )

                // Seleção de Status
                SectionLabel(text = "Status de Leitura")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BookStatus.entries.forEach { bookStatus ->
                        val isSelected = status == bookStatus
                        val color = statusColor(bookStatus)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) color else Color.White)
                                .then(
                                    if (!isSelected) Modifier.shadow(1.dp, RoundedCornerShape(12.dp)) else Modifier
                                )
                        ) {
                            TextButton(
                                onClick = { status = bookStatus },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when(bookStatus) {
                                        BookStatus.WANT_TO_READ -> "🔖\nQuero Ler"
                                        BookStatus.READING      -> "📖\nLendo"
                                        BookStatus.READ         -> "✅\nLido"
                                    },
                                    color = if (isSelected) Color.White else Color(0xFF757575),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Avaliação com estrelas
                SectionLabel(text = "Avaliação")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(5) { i ->
                        IconButton(
                            onClick = { rating = if (rating == i + 1) 0 else i + 1 },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = if (i < rating) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "${i + 1} estrela(s)",
                                tint = if (i < rating) Color(0xFFFFC107) else Color(0xFFBDBDBD),
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    AnimatedVisibility(visible = rating > 0) {
                        Text(
                            text = when(rating) {
                                1 -> "😕 Não gostei"
                                2 -> "😐 Regular"
                                3 -> "🙂 Gostei"
                                4 -> "😊 Muito bom!"
                                5 -> "🤩 Incrível!"
                                else -> ""
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF616161),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Notas
                SectionLabel(text = "Notas & Impressões")
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Escreva suas impressões, frases favoritas,\nrecomendaria para quem...",
                            fontSize = 13.sp,
                            color = Color(0xFFBDBDBD),
                            lineHeight = 20.sp
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoPrimary,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )

                Spacer(Modifier.height(8.dp))
            }

            // ── Botão de salvar fixo no rodapé ──
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = {
                        titleError  = title.isBlank()
                        authorError = author.isBlank()
                        if (!titleError && !authorError) {
                            isSaving = true
                            val book = Book(
                                id = existingBook?.id ?: 0,
                                title  = title.trim(),
                                author = author.trim(),
                                status = status,
                                rating = rating,
                                notes  = notes.trim(),
                                createdAt = existingBook?.createdAt ?: System.currentTimeMillis()
                            )
                            viewModel.saveBook(book)
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isEditing) "Salvar Alterações" else "Adicionar à Coleção",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ── Preview da capa enquanto digita ──
@Composable
fun BookPreviewCard(title: String, author: String, status: BookStatus) {
    val color = statusColor(status)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MenuBook, null, tint = color, modifier = Modifier.size(26.dp))
        }
        Column {
            Text(
                text = title.ifBlank { "Título do livro" },
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (title.isBlank()) Color(0xFFBDBDBD) else Color(0xFF1A1A2E)
            )
            Text(
                text = author.ifBlank { "Nome do autor" },
                fontSize = 12.sp,
                color = if (author.isBlank()) Color(0xFFBDBDBD) else Color(0xFF757575),
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(Modifier.height(6.dp))
            StatusBadge(status)
        }
    }
}

@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFFBDBDBD)) },
        leadingIcon = { Icon(icon, null) },
        isError = isError,
        supportingText = { if (isError) Text(errorMessage, color = MaterialTheme.colorScheme.error) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IndigoPrimary,
            focusedLabelColor = IndigoPrimary,
            unfocusedBorderColor = Color(0xFFE0E0E0)
        )
    )
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF616161),
        letterSpacing = 0.5.sp
    )
}