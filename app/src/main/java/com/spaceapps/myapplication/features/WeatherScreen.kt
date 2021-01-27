package com.spaceapps.myapplication.features

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.MyApplicationTheme

val dayGradient = listOf(
    Color(0xff4287f5),
    Color(0xff42a4f5),
    Color(0xff42daf5)
)

private val nightGradient = listOf(
    Color(0xff05070f),
    Color(0xff0f152e),
    Color(0xFF1B2453)
)

@Composable
fun WeatherScreen() {
    val gradient = if (isSystemInDarkTheme()) nightGradient else dayGradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradient))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val selectedTab = mutableStateOf(0)
            TabRow(selectedTabIndex = selectedTab.value) {
                Tab(selected = selectedTab.value == 0, onClick = { selectedTab.value = 0 }) {

                }
                Tab(selected = selectedTab.value == 1, onClick = { selectedTab.value = 1 }) {

                }
                Tab(selected = selectedTab.value == 2, onClick = { selectedTab.value = 2 }) {

                }
            }
            Text(
                text = "Khmelnytskyi, UA",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(top = 36.dp),
                style = AmbientTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Image(
                imageVector = vectorResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .width(240.dp)
                    .height(240.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp)
            )
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text(
                    text = stringResource(R.string.temperature_value, 22f),
                    color = Color.White,
                    fontSize = 48.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Rainy",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "22 km/h",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..5) {
                    item { DayWeather() }
                }
            }
        }
    }
}

@Composable
fun DayWeather() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = vectorResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "TUE",
            color = Color.White
        )
        Text(
            text = stringResource(R.string.temperature_value, 18f),
            color = Color.LightGray
        )
        Text(
            text = stringResource(R.string.temperature_value, 5f),
            color = Color.DarkGray
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_3A)
@Composable
fun Preview() {
    MyApplicationTheme {
        WeatherScreen()
    }
}