package com.example.appubicaciones.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.PlaceComment
import com.example.appubicaciones.data.model.PlaceReply
import com.example.appubicaciones.data.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {

    private val repository = CommentRepository()

    private val _comments = MutableStateFlow<List<PlaceComment>>(emptyList())
    val comments: StateFlow<List<PlaceComment>> = _comments

    private val _replies = MutableStateFlow<List<PlaceReply>>(emptyList())
    val replies: StateFlow<List<PlaceReply>> = _replies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedComment = MutableStateFlow<PlaceComment?>(null)
    val selectedComment: StateFlow<PlaceComment?> = _selectedComment

    // 🔹 Cargar comentarios de un lugar
    fun loadCommentsForPlace(placeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _comments.value = repository.getCommentsByPlace(placeId)
            _isLoading.value = false
        }
    }

    fun loadComment(commentId: String) {
        viewModelScope.launch {
            _selectedComment.value = repository.getCommentById(commentId)
        }
    }


    // 🔹 Agregar un comentario
    fun addComment(comment: PlaceComment) {
        viewModelScope.launch {
            val success = repository.addComment(comment)
            if (success) loadCommentsForPlace(comment.placeId)
        }
    }

    // 🔹 Eliminar un comentario
    fun deleteComment(commentId: String, placeId: String) {
        viewModelScope.launch {
            val success = repository.deleteComment(commentId)
            if (success) loadCommentsForPlace(placeId)
        }
    }

    // 🔹 Cargar respuestas de un comentario
    fun loadReplies(commentId: String) {
        viewModelScope.launch {
            _replies.value = repository.getRepliesByComment(commentId)
        }
    }

    // 🔹 Agregar una respuesta
    fun addReply(reply: PlaceReply) {
        viewModelScope.launch {
            val success = repository.addReply(reply)
            if (success) loadReplies(reply.placeCommentId)
        }
    }

    // 🔹 Eliminar una respuesta
    fun deleteReply(replyId: String, commentId: String) {
        viewModelScope.launch {
            val success = repository.deleteReply(replyId)
            if (success) loadReplies(commentId)
        }
    }
}
