package com.spaceapps.myapplication.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.spaceapps.myapplication.R

typealias OnMapLoaded = (GoogleMap) -> Unit

@Composable
fun GoogleMap(
    manager: FragmentManager,
    modifier: Modifier = Modifier,
    onMapLoaded: OnMapLoaded
) {
    val mapFragment = remember { SupportMapFragment() }
    AndroidView(
        factory = {
            val fragmentContainerView = FragmentContainerView(it).apply { id = R.id.googleMapFragment }
            manager.beginTransaction().replace(R.id.googleMapFragment, mapFragment).commit()
            fragmentContainerView
        },
        modifier = modifier,
        update = { mapFragment.getMapAsync(onMapLoaded) }
    )
}
