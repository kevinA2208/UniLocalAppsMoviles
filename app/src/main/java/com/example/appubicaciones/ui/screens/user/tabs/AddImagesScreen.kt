package com.example.appubicaciones.ui.screens.user.tabs

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddImagesScreen(
    initialImages: List<Uri> = emptyList(),
    onSaveImages: (List<Uri>) -> Unit,
    onBack: () -> Unit = {}
) {
    var images by remember { mutableStateOf(initialImages) }
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
        topBar = {
            TopAppBar(
                title = { Text("Añadir imágenes del lugar") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(inner),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Text("Imágenes seleccionadas: ${images.size}", style = MaterialTheme.typography.labelMedium)
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
                            contentDescription = "Imagen ${index + 1}",
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
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior") }

                IconButton(
                    enabled = images.isNotEmpty() && index < images.lastIndex,
                    onClick = { index++ }
                ) { Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente") }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cargar nueva imagen")
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Para la aprobación del lugar en UniLocal,\n" +
                        "deberá añadir mínimo 1 imagen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onSaveImages(images) },
                enabled = images.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF61C38A)),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar imágenes") }
        }
    }


}