package com.spaceapps.myapplication.ui.views

import androidx.compose.runtime.Composable
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
    AndroidView(
        factory = {
            FragmentContainerView(it).apply {
                id = R.id.googleMapFragment
                manager.beginTransaction()
                    .add(R.id.googleMapFragment, SupportMapFragment())
                    .commit()
            }
        },
        modifier = modifier
    ) {
        (
            manager.findFragmentById(R.id.googleMapFragment)
                as? SupportMapFragment
            )?.getMapAsync(onMapLoaded)
    }
}
