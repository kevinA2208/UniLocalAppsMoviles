package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.City
import com.example.appubicaciones.ui.screens.generics.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    initialNames: String = "Kevin Andres",
    initialLastnames: String = "Usama Trespalacios",
    initialUsername: String = "KevinA2208",
    initialCity: String = "Armenia",
    onSaveClick: (names: String, lastnames: String, username: String, city: String) -> Unit = { _, _, _, _ -> }
) {
    val scrollState = rememberScrollState()

    // Estados de los campos
    var names by remember { mutableStateOf(initialNames) }
    var lastnames by remember { mutableStateOf(initialLastnames) }
    var username by remember { mutableStateOf(initialUsername) }
    var selectedCity by remember { mutableStateOf(City.entries.first { it.displayName == initialCity }) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.profile_edit),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos editables
        EditProfileField(label = stringResource(R.string.txt_firstname), value = names) { names = it }
        EditProfileField(label = stringResource(R.string.txt_lastname), value = lastnames) { lastnames = it }
        EditProfileField(label = stringResource(R.string.txt_username), value = username) { username = it }

        DropdownSelector(
            label = stringResource(R.string.txt_user_city),
            options = City.entries,
            selectedOption = selectedCity,
            onOptionSelected = { selectedCity = it },
            getOptionLabel = { it.displayName }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSaveClick(names, lastnames, username, selectedCity.displayName) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7237EC)
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.profile_save_changes))
        }
    }
}

@Composable
fun EditProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
