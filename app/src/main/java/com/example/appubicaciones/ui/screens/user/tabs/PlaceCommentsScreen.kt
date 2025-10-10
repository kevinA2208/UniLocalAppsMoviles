package com.example.appubicaciones.ui.screens.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.R

@Composable
fun PlaceCommentsScreen(
    placeName: String = "Lugar 1",
    comments: List<Pair<String, String>> = listOf(
        stringResource(R.string.place_comments_user) to stringResource(R.string.place_comments_comment),
        stringResource(R.string.place_comments_user) to stringResource(R.string.place_comments_comment),
        stringResource(R.string.place_comments_user) to stringResource(R.string.place_comments_comment),
        stringResource(R.string.place_comments_user) to stringResource(R.string.place_comments_comment)
    ),
    onCommentClick: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp)
    ) {
        // Título superior
        Text(
            text = stringResource(R.string.place_comments_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del lugar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de comentarios
        comments.forEachIndexed { index, (user, comment) ->
            CommentCard(
                userName = user,
                comment = comment,
                onClick = { onCommentClick(index) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CommentCard(
    userName: String,
    comment: String,
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
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = comment,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.place_comments_view_comment),
                tint = Color.Black
            )
        }
    }
}
