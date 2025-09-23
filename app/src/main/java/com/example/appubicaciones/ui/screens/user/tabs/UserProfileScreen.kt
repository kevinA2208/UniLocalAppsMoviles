package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appubicaciones.R

@Composable
fun UserProfileScreen(
    names: String = "Kevin Andres",
    lastnames: String = "Usama Trespacios",
    username: String = "KevinA2208",
    email: String = "usamo489@gmail.com",
    city: String = "Armenia",
    tabNavController: NavHostController,
    onEditClick: () -> Unit = {},
    onRecoverPasswordClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileField(label = stringResource(R.string.txt_firstname), value = names)

        ProfileField(label = stringResource(R.string.txt_lastname), value = lastnames)

        ProfileField(label = stringResource(R.string.txt_username), value = username)

        ProfileField(label = stringResource(R.string.txt_email), value = email)

        ProfileField(label = stringResource(R.string.txt_user_city), value = city)

        Spacer(modifier = Modifier.height(20.dp))

        // Botón Editar datos
        Button(
            onClick = onEditClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7237EC)
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = stringResource(R.string.profile_edit))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Recuperar contraseña
        Text(
            text = stringResource(R.string.txt_recover_password),
            color = Color.Gray,
            modifier = Modifier
                .clickable { onRecoverPasswordClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón historial de lugares
        Button(
            onClick = onHistoryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7237EC)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.profile_places_created))
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 5.dp)
    ) {
        Text(text = label)
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}