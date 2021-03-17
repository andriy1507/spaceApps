package com.spaceapps.myapplication.features.termsPolicy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_4
import com.spaceapps.myapplication.ui.SPACING_8
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun LegalScreen(vm: LegalViewModel) = Box(modifier = Modifier.fillMaxSize()) {
    val content by vm.content.observeAsState()
    TopAppBar(
        modifier = Modifier.statusBarsHeight(ACTION_BAR_SIZE.dp),
        contentPadding = LocalWindowInsets.current.statusBars.toPaddingValues(
            additionalStart = SPACING_8.dp,
            additionalEnd = SPACING_8.dp
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = when (vm.type) {
                LegalType.TermsOfUse -> stringResource(id = R.string.terms_of_use)
                LegalType.PrivacyPolicy -> stringResource(id = R.string.privacy_policy)
            }
        )
    }
    if (content == null) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    } else {
        LazyColumn(
            modifier = Modifier.padding(
                LocalWindowInsets.current.statusBars.toPaddingValues(
                    additionalTop = ACTION_BAR_SIZE.dp
                )
            ),
            contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues(
                additionalEnd = SPACING_8.dp,
                additionalStart = SPACING_8.dp,
                additionalTop = SPACING_4.dp,
                additionalBottom = SPACING_4.dp,
            )
        ) {
            item {
                Text(text = content.orEmpty())
            }
        }
    }
}