package com.example.appubicaciones.ui.screens.services

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appubicaciones.R

@Composable
fun AddImageProductServiceScreen(navController: NavController) {
    var images by remember { mutableStateOf(listOf<Uri>()) }
    var index by remember { mutableStateOf(0) }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            images = images + uri
            index = images.lastIndex
        }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.add_images_save))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.add_images_selected_count, images.size),
                style = MaterialTheme.typography.labelMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (images.isNotEmpty()) {
                        AsyncImage(
                            model = images[index],
                            contentDescription = stringResource(R.string.add_images_image_description, index + 1),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = rememberVectorPainter(Icons.Filled.Image),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    enabled = images.isNotEmpty() && index > 0,
                    onClick = { index-- }
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.add_images_previous)) }

                IconButton(
                    enabled = images.isNotEmpty() && index < images.lastIndex,
                    onClick = { index++ }
                ) { Icon(Icons.Filled.ArrowForward, contentDescription = stringResource(R.string.add_images_next)) }
            }

            Button(
                onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(stringResource(R.string.add_images_load_new))
            }

            if (images.isEmpty()) {
                Text(
                    text = stringResource(R.string.add_images_services_instructions),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}