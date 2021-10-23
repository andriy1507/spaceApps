package com.spaceapps.myapplication.app

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screens(val route: String) {
    object Auth : Screens("auth")
    object ForgotPassword : Screens("forgotPassword")
    object ResetPassword : Screens("resetPassword?email={email}&code={code}") {
        fun createRoute(email: String, code: String) = "resetPassword?email=$email&code=$code"
    }
    object SocialAuth : Screens("socialAuth")
}

sealed class DeepLinks(val uri: String, val args: List<NamedNavArgument>) {
    object ResetPassword : DeepLinks(
        uri = "$DEEP_LINK_URI/reset-password?email={email}&code={code}",
        args = listOf(
            navArgument("email") { type = NavType.StringType },
            navArgument("code") { type = NavType.StringType }
        )
    )
}

sealed class GeolocationGraph(route: String) : Screens(route) {
    object GeolocationMap : GeolocationGraph("geolocationMap")

    object MapSettings : GeolocationGraph("mapSettings")

    object SaveLocation : GeolocationGraph("saveLocation")

    object LocationsList : GeolocationGraph("locationsList")

    companion object {
        const val route = "geolocation"
    }
}

sealed class AboutGraph(route: String) : Screens(route) {
    object About : AboutGraph("about")

    object TermsPolicy : AboutGraph("termsPolicy")

    companion object {
        const val route = "aboutGraph"
    }
}

sealed class ProfileGraph(route: String) : Screens(route) {
    object Profile : ProfileGraph("profile")

    object Notifications : ProfileGraph("notifications")

    object NotificationView : ProfileGraph("notificationView/{notificationId}/{title}") {
        fun createRoute(id: Int, title: String) = "notificationView/$id/$title"
    }

    object Devices : ProfileGraph("devices")

    companion object {
        const val route = "profileGraph"
    }
}
