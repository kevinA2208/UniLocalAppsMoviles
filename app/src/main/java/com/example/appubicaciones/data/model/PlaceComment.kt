package com.example.appubicaciones.data.model

data class PlaceComment(
    val id: String = "",
    val commentText: String = "",
    val date: String = "",
    val placeId: String = "",
    val userId: String = "",
    val userName: String? = null
)

data class PlaceReply(
    val id: String = "",
    val replyText: String = "",
    val date: String = "",
    val placeCommentId: String = "",
    val userId: String = "",
    val userName: String? = null
)