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
fun ServiceScreen(navController: NavController, products: List<ProductService>) {
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
            FloatingActionButton(onClick = {
                    navController.navigate(UserRouteTab.CreatePlace)
                }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = stringResource(R.string.txt_services),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            LazyColumn(contentPadding = padding) {

                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(UserRouteTab.CreatePlace)
                            }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(product.images.firstOrNull())
                                    .crossfade(true)
                                    .listener(
                                        onError = { _, result ->
                                            Log.e("Coil", "Error cargando ${product.name}: ${result.throwable}")
                                        }
                                    )
                                    .build(),
                                imageLoader = imageLoader,
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp).padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Ver detalle",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(all = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
