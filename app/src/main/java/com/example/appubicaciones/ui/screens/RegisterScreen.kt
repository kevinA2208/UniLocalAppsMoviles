package com.example.appubicaciones.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.R

enum class CityEnum(val displayName: String) {
    BOGOTA("Bogotá"),
    MEDELLIN("Medellín"),
    CALI("Cali"),
    BARRANQUILLA("Barranquilla"),
    CARTAGENA("Cartagena")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, CityEnum, String) -> Unit,
    onLoginClick: () -> Unit
) {
    var names by remember { mutableStateOf("") }
    var lastNames by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Dropdown city
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<CityEnum?>(null) }

    // Validaciones
    val isNamesError = names.isBlank() || names.any { it.isDigit() }
    val isLastNamesError = lastNames.isBlank() || lastNames.any { it.isDigit() }
    val isUsernameError = username.isBlank() || username.contains(" ")
    val isEmailError = email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordError = password.isBlank() || password.length < 5
    val isCityError = selectedCity == null

    val isFormValid = !isNamesError && !isLastNamesError && !isUsernameError &&
            !isEmailError && !isPasswordError && !isCityError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            color = Color(0xFF6A1B9A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.txt_register))

        Spacer(modifier = Modifier.height(16.dp))

        // Nombres
        OutlinedTextField(
            value = names,
            onValueChange = { names = it },
            label = { Text(stringResource(R.string.txt_firstname)) },
            isError = isNamesError,
            supportingText = {
                if (isNamesError) Text(stringResource(R.string.error_firstname))
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Apellidos
        OutlinedTextField(
            value = lastNames,
            onValueChange = { lastNames = it },
            label = { Text(stringResource(R.string.txt_lastname)) },
            isError = isLastNamesError,
            supportingText = {
                if (isLastNamesError) Text(stringResource(R.string.error_lastname))
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.txt_username)) },
            isError = isUsernameError,
            supportingText = {
                if (isUsernameError) Text(stringResource(R.string.error_username))
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.txt_email)) },
            isError = isEmailError,
            supportingText = {
                if (isEmailError) Text(stringResource(R.string.error_email))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown Ciudad
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCity?.displayName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.txt_user_city)) },
                isError = isCityError,
                supportingText = {
                    if (isCityError) Text(stringResource(R.string.error_city))
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CityEnum.entries.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city.displayName) },
                        onClick = {
                            selectedCity = city
                            expanded = false
                        }
                    )
                }
            }
        }

        // Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.txt_password)) },
            isError = isPasswordError,
            supportingText = {
                if (isPasswordError) Text(stringResource(R.string.error_password))
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                selectedCity?.let {
                    onRegisterClick(names, lastNames, username, email, it, password)
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
        ) {
            Text(stringResource(R.string.txt_register))
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onLoginClick) {
            Text(stringResource(R.string.txt_login), color = Color.Gray)
        }
    }
}
