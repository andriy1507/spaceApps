package com.spaceapps.myapplication.features.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.*
import com.spaceapps.myapplication.ui.SPACING_48
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.items

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val scalingLazyListState = rememberScalingLazyListState()
    val menuItems by viewModel.menuItems.collectAsState()
    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
    ) {
        ScalingLazyColumn(
            state = scalingLazyListState,
            contentPadding = PaddingValues(vertical = SPACING_48, horizontal = SPACING_8)
        ) {
            items(menuItems) {
                Chip(
                    label = { Text(text = stringResource(id = it.titleRes)) },
                    onClick = { viewModel.onItemClick(it.route) }
                )
            }
        }
    }
}
