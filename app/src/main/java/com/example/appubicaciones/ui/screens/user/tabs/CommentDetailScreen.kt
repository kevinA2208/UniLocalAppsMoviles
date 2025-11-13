package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
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
import com.example.appubicaciones.data.model.PlaceReply
import com.example.appubicaciones.ui.screens.comments.CommentCard
import com.example.appubicaciones.viewmodel.CommentViewModel

@Composable
fun CommentDetailScreen(
    placeId: String,
    commentId: String,
    userId: String,
    userName: String,
    onBack: () -> Unit = {}
) {

    val commentViewModel: CommentViewModel = viewModel()
    val selectedComment by commentViewModel.selectedComment.collectAsState()
    val replies by commentViewModel.replies.collectAsState()
    val scrollState = rememberScrollState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var replyToDeleteId by remember { mutableStateOf<String?>(null) }

    // Cargar respuestas del comentario actual
    LaunchedEffect(commentId) {
        commentViewModel.loadComment(commentId)
        commentViewModel.loadReplies(commentId)
    }

    var replyText by remember { mutableStateOf("") }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar respuesta") },
            text = { Text("¿Estás seguro de que quieres eliminar esta respuesta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        replyToDeleteId?.let { id ->
                            commentViewModel.deleteReply(id, commentId)
                        }
                        showDeleteDialog = false
                        replyToDeleteId = null
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
            .padding(vertical = 12.dp)
            .verticalScroll(scrollState)
    ) {
        // Título
        Text(
            text = stringResource(R.string.comment_detail_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Comentario original

        if (selectedComment != null) {
            Text(
                text = "Comentario Original:",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Reutilizamos tu CommentCard.
            // El onClick lo dejamos vacío porque ya estamos en el detalle.
            CommentCard(
                userName = selectedComment!!.userName ?: "Usuario desconocido",
                comment = selectedComment!!.commentText,
                isOwner = false,
                showArrow = false,
                showDelete = false,
                onDeleteClick = {},
                onClick = { }
            )
        } else {
            // Skeleton loading o texto de carga
            Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)


        Text(
            text = "Respuestas:",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // 🔹 Listado de respuestas
        if (replies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay respuestas.", color = Color.Gray)
            }
        } else {
            replies.forEach { reply ->

                val isOwner = (reply.userId == userId && userId.isNotEmpty())
                ReplyCard(
                    userName = reply.userName,
                    replyText = reply.replyText,
                    isOwner = isOwner,
                    onDeleteClick = {
                        replyToDeleteId = reply.id
                        showDeleteDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Campo para escribir nueva respuesta
        if (userId.isNotEmpty()) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                label = { Text("Escribe una respuesta...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (replyText.isNotBlank()) {
                        val newReply = PlaceReply(
                            replyText = replyText,
                            date = System.currentTimeMillis().toString(),
                            placeCommentId = commentId,
                            userId = userId,
                            userName = userName
                        )
                        commentViewModel.addReply(newReply)
                        replyText = "" // limpiar campo
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Enviar")
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Debes iniciar sesión para responder.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ReplyCard(
    userName: String?,
    replyText: String,
    isOwner: Boolean,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Fila para el Nombre y el Ícono de eliminar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userName != null) {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f) // Empuja el ícono a la derecha
                    )
                }

                // Solo mostramos el ícono si es el dueño
                if (isOwner) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Asegúrate de importar Icons.Default.Delete
                        contentDescription = "Eliminar respuesta",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDeleteClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(replyText, fontSize = 14.sp)
        }
    }
}