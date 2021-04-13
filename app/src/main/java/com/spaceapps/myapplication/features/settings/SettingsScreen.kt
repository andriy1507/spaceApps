package com.spaceapps.myapplication.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.FONT_16
import com.spaceapps.myapplication.ui.FONT_24
import com.spaceapps.myapplication.ui.SPACING_16
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun SettingsScreen(vm: SettingsViewModel) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
) {
    val events by vm.events.collectAsState(initial = InitSettingsState)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues(additionalBottom = ACTION_BAR_SIZE.dp)
    ) {
        item {
            Button(onClick = vm::showLogOut) {
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
    if (events is ShowLogOutDialog) {
        LogOutDialog(dismiss = vm::dismissLogOut, logOut = vm::logOut)
    }
}

@Preview
@Composable
fun LogOutDialog(
    dismiss: () -> Unit = {},
    logOut: () -> Unit = {}
) = Dialog(onDismissRequest = dismiss) {
    Column(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .wrapContentHeight()
            .clip(RoundedCornerShape(SPACING_16.dp))
            .background(MaterialTheme.colors.background)
            .padding(SPACING_16.dp)
    ) {
        Text(text = "Log out", fontSize = FONT_24.sp)
        Text(text = "Are you sure you want log out?", fontSize = FONT_16.sp)
        Row {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = dismiss) {
                Text(text = "Cancel")
            }
            TextButton(onClick = logOut) {
                Text(text = "Ok")
            }
        }
    }
}
