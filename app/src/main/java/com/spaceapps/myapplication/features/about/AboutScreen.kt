package com.spaceapps.myapplication.features.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.statusBarsPadding
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.R

@Composable
fun AboutScreen() {
    Column(modifier = Modifier.statusBarsPadding()) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.version)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = BuildConfig.VERSION_NAME
            )
        }
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.terms_of_use)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.privacy_policy)
            )
        }
    }
}