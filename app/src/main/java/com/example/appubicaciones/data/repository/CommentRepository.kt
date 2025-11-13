package com.example.appubicaciones.data.repository

import com.example.appubicaciones.data.model.PlaceComment
import com.example.appubicaciones.data.model.PlaceReply
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CommentRepository {

    private val db = FirebaseFirestore.getInstance()
    private val commentsRef = db.collection("place_comments")
    private val repliesRef = db.collection("place_replies")

    private val usersRef = db.collection("users")

    // 🔹 Obtener comentarios por lugar
    suspend fun getCommentsByPlace(placeId: String): List<PlaceComment> {
        return try {
            val commentDocs = commentsRef
                .whereEqualTo("placeId", placeId)
                .get()
                .await()
                .documents

            val comments = commentDocs.mapNotNull { doc ->
                doc.toObject(PlaceComment::class.java)?.copy(id = doc.id)
            }

            if (comments.isEmpty()) return comments

            // 🔹 Obtener los IDs de usuario únicos de los comentarios
            val userIds = comments.mapNotNull { it.userId }.distinct()

            // 🔹 Traer los usernames desde la colección de usuarios
            val userDocs = usersRef
                .whereIn("id", userIds)
                .get()
                .await()
                .documents

            val userMap = userDocs.associate { doc ->
                val id = doc.getString("id") ?: doc.id
                val username = doc.getString("username") ?: "Usuario desconocido"
                id to username
            }

            // 🔹 Combinar username + comentario
            comments.map { comment ->
                comment.copy(
                    userName = userMap[comment.userId] ?: "Usuario desconocido"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 🔹 Agregar un comentario
    suspend fun addComment(comment: PlaceComment): Boolean {
        return try {
            commentsRef.add(comment).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // 🔹 Eliminar un comentario
    suspend fun deleteComment(commentId: String): Boolean {
        return try {
            commentsRef.document(commentId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getCommentById(commentId: String): PlaceComment? {
        return try {
            val doc = commentsRef.document(commentId).get().await()
            val comment = doc.toObject(PlaceComment::class.java)?.copy(id = doc.id) ?: return null

            // Buscar el nombre del usuario
            val userDoc = usersRef.document(comment.userId).get().await()
            val username = userDoc.getString("username") ?: "Usuario desconocido"

            comment.copy(userName = username)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 🔹 Obtener respuestas de un comentario
    suspend fun getRepliesByComment(commentId: String): List<PlaceReply> {
        return try {
            repliesRef
                .whereEqualTo("placeCommentId", commentId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(PlaceReply::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 🔹 Agregar una respuesta
    suspend fun addReply(reply: PlaceReply): Boolean {
        return try {
            repliesRef.add(reply).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // 🔹 Eliminar una respuesta
    suspend fun deleteReply(replyId: String): Boolean {
        return try {
            repliesRef.document(replyId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}


