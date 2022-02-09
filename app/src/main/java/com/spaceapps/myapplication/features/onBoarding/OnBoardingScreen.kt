package com.spaceapps.myapplication.features.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.features.onBoarding.model.OnBoardingPageModel
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_24
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val NEXT_PAGE_DELAY = 3000L
private const val IMAGE_WEIGHT = .6f
private const val TEXT_WEIGHT = .4f

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(onActionSubmit: OnActionSubmit) {
    val pages = remember {
        buildList {
            repeat(5) {
                add(
                    OnBoardingPageModel(
                        imageRes = R.drawable.ic_launcher_foreground,
                        titleRes = R.string.app_name,
                        textRes = R.string.app_name
                    )
                )
            }
        }
    }
    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState.currentPage) {
        launch {
            delay(NEXT_PAGE_DELAY)
            pagerState.apply {
                val targetPage = if (currentPage < pageCount - 1) currentPage + 1 else 0
                animateScrollToPage(targetPage)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = pages.size,
            state = pagerState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Image(
                    modifier = Modifier
                        .weight(IMAGE_WEIGHT)
                        .fillMaxWidth(),
                    painter = painterResource(pages[it].imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Icons.Filled.Mail
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(TEXT_WEIGHT),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = pages[it].titleRes),
                        style = MaterialTheme.typography.h4
                    )
                    Text(
                        text = stringResource(id = pages[it].textRes),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = SPACING_16,
                    end = SPACING_16,
                    bottom = SPACING_24,
                ),
            onClick = { onActionSubmit(OnBoardingAction.ContinueClick) },
            contentPadding = PaddingValues(SPACING_16)
        ) {
            Text(text = "Continue")
        }
    }
}
