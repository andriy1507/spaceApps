package com.spaceapps.myapplication.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.*
import com.spaceapps.myapplication.core.*
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_4
import com.spaceapps.myapplication.ui.SPACING_8

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                content = {
                    IconButton(onClick = viewModel::goBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                    Spacer(modifier = Modifier.width(SPACING_8))
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.h6
                    )
                },
                contentPadding = rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(SPACING_16)
        ) {
            Text(
                modifier = Modifier.padding(vertical = SPACING_16),
                text = stringResource(R.string.coordinates_system),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary
            )
            val coordinatesSystem by viewModel.system.collectAsState()
            val formats = listOf(DEGREES_DMS, DEGREES_DECIMAL)
            val systems = listOf(
                SYSTEM_GEO,
//                SYSTEM_S43,
//                SYSTEM_S63,
//                SYSTEM_UTM
            )
            for (s in systems) {
                val textId = when (s) {
                    SYSTEM_GEO -> R.string.system_geo
                    SYSTEM_S43 -> R.string.system_s43
                    SYSTEM_S63 -> R.string.system_s63
                    SYSTEM_UTM -> R.string.system_utm
                    else -> throw IllegalArgumentException("Unsupported coordinate system")
                }
                Row(
                    Modifier.padding(vertical = SPACING_4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = coordinatesSystem == s,
                        onClick = { viewModel.onSystemClick(s) }
                    )
                    Text(
                        modifier = Modifier.padding(start = SPACING_4),
                        text = stringResource(id = textId),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
            val degreesFormat by viewModel.format.collectAsState()
            Text(
                modifier = Modifier.padding(vertical = SPACING_16),
                text = stringResource(R.string.degrees_format),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary
            )
            for (f in formats) {
                val textId = when (f) {
                    DEGREES_DMS -> R.string.degrees_dms
                    DEGREES_DECIMAL -> R.string.degrees_decimal
                    else -> throw IllegalArgumentException("Unsupported degrees format")
                }
                Row(
                    Modifier.padding(vertical = SPACING_4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = degreesFormat == f, onClick = { viewModel.onFormatClick(f) })
                    Text(
                        modifier = Modifier.padding(start = SPACING_4),
                        text = stringResource(id = textId),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }
    }
}
