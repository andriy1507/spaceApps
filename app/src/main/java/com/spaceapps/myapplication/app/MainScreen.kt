package com.spaceapps.myapplication.app

import android.location.Location
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.app.Navigation.chat
import com.spaceapps.myapplication.app.Navigation.geolocation
import com.spaceapps.myapplication.app.Navigation.notifications
import com.spaceapps.myapplication.app.Navigation.qrCode
import com.spaceapps.myapplication.app.Navigation.settings
import com.spaceapps.myapplication.features.chat.ChatScreen
import com.spaceapps.myapplication.features.geolocation.GeolocationScreen
import com.spaceapps.myapplication.features.geolocation.GeolocationViewModel
import com.spaceapps.myapplication.features.notifications.NotificationsScreen
import com.spaceapps.myapplication.features.settings.SettingsScreen
import com.spaceapps.myapplication.features.settings.SettingsViewModel
import com.spaceapps.myapplication.ui.FONT_10
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.SPACING_2
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.NavigationDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Preview
@Composable
fun MainScreen(navDispatcher: NavigationDispatcher = NavigationDispatcher()) = SpaceAppsTheme {
    val navController = rememberNavController()
    LaunchedEffect(navController) {
        navDispatcher.emitter.onEach { command ->
            command(navController)
        }.launchIn(this)
    }
    Scaffold(
        bottomBar = {
            when (currentRoute(navController)) {
                settings,
                geolocation,
                notifications -> BottomBar(navController)
                else -> Unit
            }
        }
    ) {
        NavHost(navController = navController, startDestination = chat) {
            composable(settings) {
                val vm = hiltViewModel<SettingsViewModel>(it)
                val language by vm.language.observeAsState(Settings.Language.UNRECOGNIZED)
                SettingsScreen(
                    language = language,
                    onLogOutClick = vm::logOut,
                    onChangeLanguage = vm::setLanguage
                )
            }
            composable(notifications) {
                NotificationsScreen()
            }
            composable(geolocation) {
                val vm = hiltViewModel<GeolocationViewModel>(it)
                val location by vm.lastLocation.observeAsState(Location(""))
                val isMapTracking by vm.isMapTracking.observeAsState(true)
                val mapType by vm.mapType.observeAsState(MAP_TYPE_NORMAL)
                GeolocationScreen(
                    location = location,
                    isMapTracking = isMapTracking,
                    onMapTrackingChange = vm::setMapTracking,
                    mapType = mapType,
                    onMapTypeChange = vm::setMapType
                )
            }
            composable(qrCode) {
            }
            composable(chat) {
                ChatScreen()
            }
        }
    }
}

@Composable
fun BottomBar(controller: NavHostController) = BottomAppBar {
    val isGeolocation = currentRoute(navController = controller) == geolocation
    NavigationItem(
        isSelected = isGeolocation,
        onClick = { controller.navigate(geolocation) },
        iconRes = R.drawable.ic_location,
        labelRes = R.string.location
    )
    val isNotifications = currentRoute(navController = controller) == notifications
    CounterNavigationItem(
        isSelected = isNotifications,
        onClick = { controller.navigate(notifications) },
        count = 25,
        iconRes = R.drawable.ic_notifications,
        labelRes = R.string.notifications
    )
    val isSettings = currentRoute(navController = controller) == settings
    NavigationItem(
        isSelected = isSettings,
        onClick = { controller.navigate(settings) },
        iconRes = R.drawable.ic_settings,
        labelRes = R.string.settings
    )
}

@Composable
fun RowScope.NavigationItem(
    isSelected: Boolean,
    onClick: OnClick,
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int
) = BottomNavigationItem(
    selected = isSelected,
    onClick = { if (!isSelected) onClick() },
    icon = {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = stringResource(labelRes)
        )
    },
    label = { Text(text = stringResource(labelRes)) }
)

@Composable
fun RowScope.CounterNavigationItem(
    isSelected: Boolean,
    onClick: OnClick,
    count: Int,
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int
) = BottomNavigationItem(
    selected = isSelected,
    onClick = { if (!isSelected) onClick() },
    icon = {
        Box {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(labelRes)
            )
            if (count > 0) {
                Text(
                    text = "$count",
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary)
                        .align(Alignment.TopEnd)
                        .padding(SPACING_2.dp),
                    fontSize = FONT_10.sp,
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    },
    label = { Text(text = stringResource(labelRes)) }
)

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
