package com.example.appubicaciones.ui.screens.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.ProductService
import com.example.appubicaciones.ui.screens.user.nav.UserRouteTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductServiceScreen(
    navController: NavController,
    onGuardar: (ProductService) -> Unit
) {
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var applyPrice by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() &&
            description.isNotBlank() &&
            (!applyPrice || (price.toDoubleOrNull() != null && price.toDouble() > 0))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_images_back)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.add_images_back))
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    nameError = name.isBlank()
                    descriptionError = description.isBlank()
                    priceError = applyPrice && (price.toDoubleOrNull() == null || price.toDouble() <= 0)

                    if (isFormValid) {
                        onGuardar(
                            ProductService(
                                id = System.currentTimeMillis().toString(),
                                name = name,
                                description = description,
                                applyPrice = applyPrice,
                                price = price.toDoubleOrNull()
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = isFormValid
            ) {
                Text(stringResource(R.string.create_product_save))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.txt_create_product_services),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) nameError = false
                },
                label = { Text(stringResource(R.string.create_product_name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError
            )
            if (nameError) {
                Text(
                    stringResource(R.string.create_product_name_error),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (it.isNotBlank()) descriptionError = false
                },
                label = { Text(stringResource(R.string.create_product_description)) },
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError
            )
            if (descriptionError) {
                Text(
                    stringResource(R.string.create_product_description_error),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(Modifier.height(12.dp))

            // ¿Aplica precio?
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.create_product_apply_price_title), fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = applyPrice, onCheckedChange = { applyPrice = it })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.create_product_apply_price_hint))
            }

            if (applyPrice) {
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        if (it.toDoubleOrNull() != null && it.toDouble() > 0) priceError = false
                    },
                    label = { Text(stringResource(R.string.create_product_price_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = priceError
                )
                if (priceError) {
                    Text(
                        stringResource(R.string.create_product_price_error),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(UserRouteTab.AddImageProductService) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.create_product_images))
            }
        }
    }
}