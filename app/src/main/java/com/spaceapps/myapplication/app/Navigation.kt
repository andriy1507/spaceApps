package com.spaceapps.myapplication.app

sealed class Screens(val route: String) {
    object Auth : Screens("auth")
    object ForgotPassword : Screens("forgotPassword")
    object SocialAuth : Screens("socialAuth")
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
