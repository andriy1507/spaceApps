package com.spaceapps.myapplication.app

sealed class Screens(val route: String) {
    object Auth : Screens("auth")
    object ForgotPassword : Screens("forgotPassword")
    object SocialAuth : Screens("socialAuth")
}

sealed class GeolocationGraph(val route: String) {
    object GeolocationMap : GeolocationGraph("geolocationMap")

    object SaveLocation : GeolocationGraph("saveLocation")

    object LocationsList : GeolocationGraph("locationsList")

    companion object {
        const val route = "geolocation"
    }
}

sealed class AboutGraph(val route: String) {
    object About : AboutGraph("about")

    object TermsPolicy : AboutGraph("termsPolicy")

    companion object {
        const val route = "aboutGraph"
    }
}
