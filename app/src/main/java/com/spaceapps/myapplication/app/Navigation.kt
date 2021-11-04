package com.spaceapps.myapplication.app

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.spaceapps.myapplication.core.DEEP_LINK_URI

sealed class Screens(val route: String) {
//    Authorization
    object Auth : Screens("auth")
    object ForgotPassword : Screens("forgotPassword")
    object ResetPassword : Screens("resetPassword?email={email}&code={code}") {
        fun createRoute(email: String, code: String) = "resetPassword?email=$email&code=$code"
    }
    object SocialAuth : Screens("socialAuth")
//    Geolocation
    object GeolocationMap : Screens("geolocationMap")
    object MapSettings : Screens("mapSettings")
    object SaveLocation : Screens("saveLocation")
    object LocationsList : Screens("locationsList")
//    About
    object About : Screens("about")
    object TermsPolicy : Screens("termsPolicy")
//    Profile
    object Profile : Screens("profile")
    object Notifications : Screens("notifications")
    object NotificationView : Screens("notificationView/{notificationId}/{title}") {
        fun createRoute(id: Int, title: String) = "notificationView/$id/$title"
    }
    object Devices : Screens("devices")
}

sealed class DeepLinks(val uri: String, val args: List<NamedNavArgument>) {
    object ResetPassword : DeepLinks(
        uri = "$DEEP_LINK_URI/reset-password?email={email}&code={code}",
        args = listOf(
            navArgument("email") { type = NavType.StringType },
            navArgument("code") { type = NavType.StringType }
        )
    )

    object NotificationView : DeepLinks(
        uri = "$DEEP_LINK_URI/notifications/{notificationId}/{title}",
        args = listOf(
            navArgument("notificationId") { type = NavType.IntType },
            navArgument("title") { type = NavType.StringType }
        )
    )
}
