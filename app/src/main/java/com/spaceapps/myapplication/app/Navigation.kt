package com.spaceapps.myapplication.app

sealed class Screens(val route: String) {
    object Auth : Screens("auth")
    object ForgotPassword : Screens("forgotPassword")
    object SocialAuth : Screens("socialAuth")
}

sealed class GeolocationGraph(val route: String) {
    object GeolocationMap : GeolocationGraph("geolocationMap")

    companion object {
        const val route = "geolocation"
    }
}
