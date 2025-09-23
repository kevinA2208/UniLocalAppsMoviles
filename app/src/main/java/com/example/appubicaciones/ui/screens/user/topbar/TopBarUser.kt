package com.example.appubicaciones.ui.screens.user.topbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUser(){
    CenterAlignedTopAppBar(
        title = {Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            color = Color(0xFF6A1B9A)
        )
        }
    )
}