package com.spaceapps.myapplication.features.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.insets.systemBarsPadding
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.Settings.Language.English
import com.spaceapps.myapplication.Settings.Language.Ukrainian
import com.spaceapps.myapplication.ui.*

private const val DIALOG_WIDTH_RATIO = .8f
typealias OnChangeLanguage = (Settings.Language) -> Unit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    language: Settings.Language,
    onLogOutClick: OnClick,
    onChangeLanguage: OnChangeLanguage
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
) {
    var showLogOutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(bottom = ACTION_BAR_SIZE.dp)
    ) {
        TextButton(onClick = { showLanguageDialog = true }) {
            Text(text = stringResource(R.string.change_language))
        }
        TextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(horizontal = SPACING_16.dp)
                .padding(bottom = SPACING_24.dp),
            onClick = { showLogOutDialog = true }
        ) {
            Text(text = stringResource(R.string.log_out))
        }
    }
    if (showLogOutDialog) LogOutDialog(
        dismiss = { showLogOutDialog = false },
        logOut = onLogOutClick
    )
    if (showLanguageDialog) LanguageDialog(
        language = language,
        onChangeLanguage = onChangeLanguage,
        onDismiss = { showLanguageDialog = false }
    )
}

@Composable
fun LanguageDialog(
    language: Settings.Language,
    onChangeLanguage: OnChangeLanguage,
    onDismiss: OnClick
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SPACING_16.dp))
                .background(MaterialTheme.colors.background)
                .padding(SPACING_16.dp)
        ) {
            LanguageButton(
                selected = language == English,
                onClick = { onChangeLanguage(English) },
                text = stringResource(R.string.english),
                painter = painterResource(R.drawable.ic_united_kingdom)
            )
            LanguageButton(
                selected = language == Ukrainian,
                onClick = { onChangeLanguage(Ukrainian) },
                text = stringResource(R.string.ukrainian),
                painter = painterResource(R.drawable.ic_ukraine)
            )
        }
    }
}

@Composable
fun LanguageButton(selected: Boolean, onClick: OnClick, text: String, painter: Painter) = Row(
    modifier = Modifier
        .padding(vertical = SPACING_8.dp)
        .clickable(onClick = onClick),
    verticalAlignment = Alignment.CenterVertically
) {
    RadioButton(selected = selected, onClick = onClick)
    Text(modifier = Modifier.padding(end = SPACING_4.dp), text = text)
    Spacer(modifier = Modifier.weight(1f))
    Image(modifier = Modifier.size(SPACING_24.dp), painter = painter, contentDescription = null)
}

@Composable
fun LogOutDialog(
    dismiss: OnClick,
    logOut: OnClick
) = Dialog(onDismissRequest = dismiss) {
    Column(
        modifier = Modifier
            .fillMaxWidth(DIALOG_WIDTH_RATIO)
            .wrapContentHeight()
            .clip(RoundedCornerShape(SPACING_16.dp))
            .background(MaterialTheme.colors.background)
            .padding(SPACING_16.dp)
    ) {
        Text(text = stringResource(R.string.log_out), fontSize = FONT_24.sp)
        Text(
            text = stringResource(R.string.are_you_sure_you_want_to_log_out),
            fontSize = FONT_16.sp
        )
        Row {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = dismiss) { Text(text = stringResource(R.string.cancel)) }
            TextButton(onClick = logOut) { Text(text = stringResource(R.string.ok)) }
        }
    }
}
