package com.spaceapps.myapplication.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}

@Composable
fun SettingsScreen() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues(additionalBottom = ACTION_BAR_SIZE.dp)
    ) {

        item {
            DropdownMenu(expanded = false, onDismissRequest = { }) {
                DropdownMenuItem(onClick = { }) {
                    Text(text = "English")
                }
                DropdownMenuItem(onClick = { }) {
                    Text(text = "Ukrainian")
                }
            }
        }
    }
}
