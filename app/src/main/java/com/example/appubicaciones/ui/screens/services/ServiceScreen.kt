package com.example.appubicaciones.ui.screens.services

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.ProductService
import com.example.appubicaciones.ui.screens.user.nav.UserRouteTab
import okhttp3.OkHttpClient

@Composable
fun ServiceScreen(
    navController: NavController,
    products: List<ProductService>,
    onViewDetailProduct: (String) -> Unit = {},
    onRefreshProducts: () -> Unit,
    placeId: String,
    placeOwnerId: String?,
    currentUserId: String?,
) {

    LaunchedEffect(placeId) {
        onRefreshProducts()
    }


    val context = LocalContext.current
    val imageLoader = remember {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "UniLocal/1.0 (https://tusitio.example; contacto: soporte@tusitio.example)"
                    )
                    .header("Accept", "image/svg+xml,image/*;q=0.8")
                    .header("Referer", "https://commons.wikimedia.org/")
                    .build()
                chain.proceed(req)
            }
            .build()

        ImageLoader.Builder(context)
            .okHttpClient(client)
            .components { add(SvgDecoder.Factory()) }
            .crossfade(true)
            .build()
    }

    Scaffold(
        floatingActionButton = {
            if (currentUserId != null && currentUserId == placeOwnerId) {
                FloatingActionButton(onClick = {
                    navController.navigate(UserRouteTab.CreateProductService(placeId))
                }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.services_add)
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = stringResource(R.string.services_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos o servicios registrados para este lugar.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onViewDetailProduct(product.id) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {

                                // Imagen
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(product.images.firstOrNull())
                                        .crossfade(true)
                                        .listener(
                                            onError = { _, result ->
                                                Log.e(
                                                    "Coil",
                                                    "Error cargando ${product.name}: ${result.throwable}"
                                                )
                                            }
                                        )
                                        .build(),
                                    imageLoader = imageLoader,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .size(90.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(16.dp))

                                // Título del producto
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    )

                                    product.description.takeIf { it.isNotBlank() }?.let {
                                        Text(
                                            text = it,
                                            maxLines = 2,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                }

                                Spacer(Modifier.width(8.dp))

                                // Flecha
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = stringResource(R.string.services_view_detail),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}