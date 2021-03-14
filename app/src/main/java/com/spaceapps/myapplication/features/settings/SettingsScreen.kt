package com.spaceapps.myapplication.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun SettingsScreen(vm: SettingsViewModel) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues(additionalBottom = ACTION_BAR_SIZE.dp)
    ) {
        item {
            Button(onClick = vm::logOut) {
                Text(text = stringResource(R.string.log_out))
            }
        }

        item {
            Button(onClick = vm::goChat) {
                Text(text = stringResource(R.string.chat))
            }
        }
        item {
            Button(onClick = vm::goFeeds) {
                Text(text = stringResource(R.string.feeds))
            }
        }
    }
}
