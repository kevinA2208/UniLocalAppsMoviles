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
import com.example.appubicaciones.data.model.City
import com.example.appubicaciones.ui.screens.generics.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, City, String) -> Unit,
    onLoginClick: () -> Unit
) {
    // Campos
    var names by remember { mutableStateOf("") }
    var lastNames by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<City?>(null) }

    // Touched flags
    var namesTouched by remember { mutableStateOf(false) }
    var lastNamesTouched by remember { mutableStateOf(false) }
    var usernameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var cityTouched by remember { mutableStateOf(false) }

    // Validaciones
    val isNamesError = namesTouched && (names.isBlank() || names.any { it.isDigit() })
    val isLastNamesError = lastNamesTouched && (lastNames.isBlank() || lastNames.any { it.isDigit() })
    val isUsernameError = usernameTouched && (username.isBlank() || username.contains(" "))
    val isEmailError = emailTouched && (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
    val isPasswordError = passwordTouched && (password.isBlank() || password.length < 5)
    val isCityError = cityTouched && selectedCity == null

    val isFormValid = !isNamesError && !isLastNamesError && !isUsernameError &&
            !isEmailError && !isPasswordError && !isCityError

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                onValueChange = {
                    names = it
                    namesTouched = true
                },
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
                onValueChange = {
                    lastNames = it
                    lastNamesTouched = true
                },
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
                onValueChange = {
                    username = it
                    usernameTouched = true
                },
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
                onValueChange = {
                    email = it
                    emailTouched = true
                },
                label = { Text(stringResource(R.string.txt_email)) },
                isError = isEmailError,
                supportingText = {
                    if (isEmailError) Text(stringResource(R.string.error_email))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown Selector para Ciudad
            DropdownSelector(
                label = stringResource(R.string.txt_user_city),
                options = City.entries,
                selectedOption = selectedCity ?: City.BOGOTA,
                onOptionSelected = {
                    selectedCity = it
                    cityTouched = true
                },
                getOptionLabel = { it.displayName }
            )

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                },
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7237EC))
            ) {
                Text(stringResource(R.string.txt_register))
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onLoginClick) {
                Text(stringResource(R.string.txt_login), color = Color.Gray)
            }
        }
    }
}
