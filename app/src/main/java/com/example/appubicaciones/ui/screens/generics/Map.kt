package com.example.appubicaciones.ui.screens.generics

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.Place
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions

@Composable
fun Map(
    modifier: Modifier = Modifier,
    activateClick: Boolean = false,
    places: List<Place> = emptyList(),
    onMapClickListener: (Point) -> Unit = {},
    zoomValue: Double = 7.0
){

    var clickedPoint by rememberSaveable { mutableStateOf<Point?>(null) }

    val context = LocalContext.current

    val hasPermission = rememberLocationPermissionState{
        Toast.makeText(
            context,
            if (it) "Ha condedido permiso para acceder a su ubicación" else "No ha concedido permiso para acceder a su ubicación",
            Toast.LENGTH_LONG
        ).show()
    }

    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource(id = R.drawable.red_marker)
    )

    var mapViewportState = rememberMapViewportState{
        setCameraOptions {
            zoom(zoomValue)
            center(Point.fromLngLat(-75.6491181, 4.4687891))
            pitch(0.0)
        }
    }

    MapboxMap(
        modifier = modifier,
        mapViewportState = mapViewportState,
        onMapClickListener = {
            if (activateClick) {
                onMapClickListener(it)
                clickedPoint = it
            }
            true
        }
    ) {

        if (hasPermission){
            MapEffect(key1 = "follow_puck") { mapView ->
                mapView.location.updateSettings {
                    locationPuck= createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }

                mapViewportState.transitionToFollowPuckState(
                    defaultTransitionOptions = DefaultViewportTransitionOptions.Builder().maxDurationMs(0).build()
                )
            }
        }

        clickedPoint?.let {
            PointAnnotation(point = it) {
                iconImage = marker
            }
        }

        if (places.isNotEmpty()){
            places.forEach { place ->
                PointAnnotation(
                    point = Point.fromLngLat(place.longitude, place.latitude)
                ){
                    iconImage = marker
                }
            }
        }
    }
}


@Composable
fun rememberLocationPermissionState(
    permission: String = android.Manifest.permission.ACCESS_FINE_LOCATION,
    onPermissionResult: (Boolean) -> Unit
): Boolean {
    val context = LocalContext.current
    var permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
        onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted.value) {
            launcher.launch(permission)
        }
    }

    return permissionGranted.value
}