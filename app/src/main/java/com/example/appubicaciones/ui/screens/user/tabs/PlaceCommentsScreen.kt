package com.example.appubicaciones.ui.screens.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.PlaceComment
import com.example.appubicaciones.viewmodel.CommentViewModel

@Composable
fun PlaceCommentsScreen(
    placeId: String,
    placeName: String,
    userId: String,
    userName: String,
    onCommentClick: (String) -> Unit = {}
) {

    val commentViewModel: CommentViewModel = viewModel()
    val comments by commentViewModel.comments.collectAsState()
    val isLoading by commentViewModel.isLoading.collectAsState()

    var showCommentInput by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    // Estados para eliminar comentario
    var showDeleteDialog by remember { mutableStateOf(false) }
    var commentToDeleteId by remember { mutableStateOf<String?>(null) }

    // Cargar los comentarios al abrir
    LaunchedEffect(placeId) {
        commentViewModel.loadCommentsForPlace(placeId)
    }

    val scrollState = rememberScrollState()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar comentario") },
            text = { Text("Si eliminas este comentario, también se eliminarán sus respuestas. ¿Estás seguro?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        commentToDeleteId?.let { id ->
                            commentViewModel.deleteComment(id, placeId)
                        }
                        showDeleteDialog = false
                        commentToDeleteId = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp)
    ) {
        // Título superior
        Text(
            text = "Comentarios de $placeName",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF673AB7))
            }
        } else if (comments.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay comentarios.", color = Color.Gray)
            }
        } else {
            comments.forEach { comment ->
                val isOwner = (userId.isNotEmpty() && comment.userId == userId)

                CommentCard(
                    userName = comment.userName ?: "Usuario desconocido",
                    comment = comment.commentText,
                    isOwner = isOwner,
                    onDeleteClick = {
                        commentToDeleteId = comment.id
                        showDeleteDialog = true
                    },
                    onClick = { onCommentClick(comment.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar comentario (solo si hay sesión)
        if (userId.isNotEmpty()) {
            if (!showCommentInput) {
                Button(
                    onClick = { showCommentInput = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1C4E9))
                ) {
                    Text("Agregar comentario", color = Color.Black)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text("Escribe tu comentario...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = {
                            showCommentInput = false
                            commentText = ""
                        }) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    val newComment = PlaceComment(
                                        commentText = commentText,
                                        date = System.currentTimeMillis().toString(),
                                        placeId = placeId,
                                        userId = userId,
                                        userName = userName
                                    )
                                    commentViewModel.addComment(newComment)
                                    commentText = ""
                                    showCommentInput = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                        ) {
                            Text("Enviar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentCard(
    userName: String,
    comment: String,
    isOwner: Boolean = false,
    showArrow: Boolean = true,
    showDelete: Boolean = true,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF4FF))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Fila para Nombre + Ícono de borrar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    // Mostrar solo si es dueño
                    if (isOwner && showDelete) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar comentario",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onDeleteClick() }
                        )
                    }
                }

                Text(
                    text = comment,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}
