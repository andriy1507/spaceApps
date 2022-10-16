package com.spaceapps.myapplication.app.activity

import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.spaceapps.navigation.Screens
import com.spaceapps.myapplication.coreui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.coreui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.navigateToRootDestination

@Composable
fun MainScreen(
    isBottomBarVisible: Boolean,
    bottomItems: List<MenuItem>,
    navController: NavHostController,
    startDestination: String
) {
    var selectedIndex by rememberSaveable(key = "selectedIndex") { mutableStateOf(0) }
    SpaceAppsTheme {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                ) {
                    BottomNavigation(
                        modifier = Modifier.navigationBarsHeight(ACTION_BAR_SIZE)
                    ) {
                        bottomItems.forEachIndexed { index, menuItem ->
                            BottomNavigationItem(
                                modifier = Modifier.navigationBarsPadding(),
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    navController.navigateToRootDestination(menuItem.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = menuItem.icon,
                                        contentDescription = stringResource(id = menuItem.labelId)
                                    )
                                },
                                label = { Text(text = stringResource(id = menuItem.labelId)) }
                            )
                        }
                    }
                }
            },
            content = {
                PopulatedNavHost(navController, startDestination, it) {
                    selectedIndex = 0
                    navController.navigateToRootDestination(Screens.GeolocationMap.route)
                }
            }
        )
    }
}
