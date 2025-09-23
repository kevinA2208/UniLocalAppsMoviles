package com.example.appubicaciones.ui.screens

import android.util.Patterns
import com.example.appubicaciones.R

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    padding: PaddingValues = PaddingValues()
) {
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.txt_login),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo correo
            OutlinedTextField(
                value = email,
                isError = isEmailError,
                supportingText = {
                    if (isEmailError) {
                        Text(text = stringResource(R.string.error_email))
                    }
                },
                onValueChange = {
                    email = it
                    isEmailError = email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                label = { Text(stringResource(R.string.txt_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo contraseña
            OutlinedTextField(
                value = password,
                isError = isPasswordError,
                supportingText = {
                    if (isPasswordError) {
                        Text(text = stringResource(R.string.error_password))
                    }
                },
                onValueChange = {
                    password = it
                    isPasswordError = password.isBlank() || password.length < 5
                },
                label = { Text(stringResource(R.string.txt_password)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Login
            Button(
                onClick = { onLoginClick(email, password) },
                enabled = !isEmailError && !isPasswordError && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7237EC))
            ) {
                Text(stringResource(R.string.txt_login))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Otras acciones
            TextButton(onClick = onRegisterClick) {
                Text(stringResource(R.string.txt_register))
            }
            TextButton(onClick = onRegisterClick) {
                Text(stringResource(R.string.txt_recover_password))
            }
        }
    }
}
