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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appubicaciones.R

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
                title = { Text(stringResource(R.string.add_images_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.add_images_back)
                        )
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

            // Conteo de imágenes seleccionadas
            Text(
                text = stringResource(R.string.add_images_selected_count, images.size),
                style = MaterialTheme.typography.labelMedium
            )

            // Vista previa de la imagen seleccionada
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
                            contentDescription = stringResource(
                                R.string.add_images_image_description,
                                index + 1
                            ),
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

            // Navegación entre imágenes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    enabled = images.isNotEmpty() && index > 0,
                    onClick = { index-- }
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.add_images_previous)
                    )
                }

                IconButton(
                    enabled = images.isNotEmpty() && index < images.lastIndex,
                    onClick = { index++ }
                ) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.add_images_next)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Botón para añadir nueva imagen
            OutlinedButton(
                onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_images_load_new))
            }

            Spacer(Modifier.height(8.dp))

            // Texto informativo
            Text(
                stringResource(R.string.add_images_instructions),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            // Botón para guardar
            Button(
                onClick = { onSaveImages(images) },
                enabled = images.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF61C38A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_images_save))
            }
        }
    }
}