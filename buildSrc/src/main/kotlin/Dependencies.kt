const val KotlinVersion = "1.5.30"
const val ComposeVersion = "1.0.3"

object AndroidX {
    object Wear {
        object Compose {
            private const val version = "1.0.0-alpha09"
            const val Material = "androidx.wear.compose:compose-material:$version"
            const val Foundation = "androidx.wear.compose:compose-foundation:$version"
            const val Navigation = "androidx.wear.compose:compose-navigation:$version"
        }
    }

    object Hilt {
        private const val hiltVersion = "1.0.0"
        const val ViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val NavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
        const val Work = "androidx.hilt:hilt-work:$hiltVersion"
        const val Compiler = "androidx.hilt:hilt-compiler:$hiltVersion"
    }

    object Compose {
        const val UiTooling = "androidx.compose.ui:ui-tooling:$ComposeVersion"
        const val Ui = "androidx.compose.ui:ui:$ComposeVersion"
        const val LiveData = "androidx.compose.runtime:runtime-livedata:$ComposeVersion"
        const val Material = "androidx.compose.material:material:$ComposeVersion"
        const val Material3 = "androidx.compose.material3:material3:1.0.0-alpha01"
        const val Icons = "androidx.compose.material:material-icons-extended:$ComposeVersion"
        const val Foundation = "androidx.compose.foundation:foundation:$ComposeVersion"
        const val Animation = "androidx.compose.animation:animation:$ComposeVersion"
    }

    object Navigation {
        const val Compose = "androidx.navigation:navigation-compose:2.4.0-beta01"
    }

    object Room {
        private const val roomVersion = "2.4.0-beta01"
        const val Runtime = "androidx.room:room-runtime:$roomVersion"
        const val Ktx = "androidx.room:room-ktx:$roomVersion"
        const val Paging = "androidx.room:room-paging:$roomVersion"
        const val Compiler = "androidx.room:room-compiler:$roomVersion"
    }

    object DataStore {
        private const val datastoreVersion = "1.0.0"
        const val DataStore = "androidx.datastore:datastore:$datastoreVersion"
        const val Preferences = "androidx.datastore:datastore-preferences:$datastoreVersion"
    }

    object CameraX {
        private const val cameraxVersion = "1.0.2"
        const val Core = "androidx.camera:camera-core:$cameraxVersion"
        const val Camera2 = "androidx.camera:camera-camera2:$cameraxVersion"
        const val Lifecycle = "androidx.camera:camera-lifecycle:$cameraxVersion"
        const val View = "androidx.camera:camera-view:1.0.0-alpha30"
    }

    object Paging {
        const val Runtime = "androidx.paging:paging-runtime-ktx:3.0.1"
        const val Compose = "androidx.paging:paging-compose:1.0.0-alpha13"
    }

    object Work {
        const val Runtime = "androidx.work:work-runtime-ktx:2.7.0"
    }

    object Core {
        const val Ktx = "androidx.core:core-ktx:1.7.0"
        const val SplashScreen = "androidx.core:core-splashscreen:1.0.0-alpha02"
    }

    object AppCompat {
        const val AppCompat = "androidx.appcompat:appcompat:1.4.0-rc01"
    }

    object Activity {
        private const val activityVersion = "1.4.0"
        const val Ktx = "androidx.activity:activity-ktx:$activityVersion"
        const val Compose = "androidx.activity:activity-compose:$activityVersion"
    }

    object StartUp {
        const val Runtime = "androidx.startup:startup-runtime:1.1.0"
    }

    object Browser {
        const val Browser = "androidx.browser:browser:1.3.0"
    }

    object Lifecycle {
        private const val lifecycleVersion = "2.4.0"
        const val ViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
    }
}


object Android {
    object Tools {
        const val Desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
        const val GradlePlugin = "com.android.tools.build:gradle:7.0.3"
    }
}

object Google {
    object Android {
        object PlayServices {
            const val Wearable = "com.google.android.gms:play-services-wearable:17.1.0"
            const val Location = "com.google.android.gms:play-services-location:18.0.0"
            const val Auth = "com.google.android.gms:play-services-auth:19.2.0"
            const val Maps = "com.google.android.gms:play-services-maps:18.0.0"
        }
    }

    object Accompanist {
        private const val accompanistVersion = "0.21.0-beta"
        const val DrawablePainter =
            "com.google.accompanist:accompanist-drawablepainter:$accompanistVersion"
        const val Insets = "com.google.accompanist:accompanist-insets:$accompanistVersion"
        const val NavigationMaterial =
            "com.google.accompanist:accompanist-navigation-material:$accompanistVersion"
        const val NavigationAnimation =
            "com.google.accompanist:accompanist-navigation-animation:$accompanistVersion"
        const val Pager = "com.google.accompanist:accompanist-pager:$accompanistVersion"
        const val PagerIndicators =
            "com.google.accompanist:accompanist-pager-indicators:$accompanistVersion"
        const val PlaceholderMaterial =
            "com.google.accompanist:accompanist-placeholder-material:$accompanistVersion"
        const val SwipeRefresh =
            "com.google.accompanist:accompanist-swiperefresh:$accompanistVersion"
        const val SystemUiController =
            "com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion"
        const val Permissions = "com.google.accompanist:accompanist-permissions:$accompanistVersion"
    }

    object Dagger {
        private const val daggerVersion = "2.39.1"
        const val HiltAndroid = "com.google.dagger:hilt-android:$daggerVersion"
        const val HiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$daggerVersion"
        const val GradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerVersion"
    }

    object Services {
        const val GradlePlugin = "com.google.gms:google-services:4.3.10"
    }

    object Firebase {
        const val CrashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:2.7.1"
        const val Bom = "com.google.firebase:firebase-bom:28.4.2"
        const val Crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val Analytics = "com.google.firebase:firebase-analytics-ktx"
        const val Messaging = "com.google.firebase:firebase-messaging-ktx"
        const val Installations = "com.google.firebase:firebase-installations"
        const val DynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
    }

    object Maps {
        private const val mapsVersion = "3.2.0"
        const val Maps = "com.google.maps.android:maps-ktx:$mapsVersion"
        const val Utils = "com.google.maps.android:maps-utils-ktx:$mapsVersion"
    }

    object ProtoBuf {
        const val JavaLite = "com.google.protobuf:protobuf-javalite:3.11.1"
    }
}

object Coil {
    private const val coilVersion = "1.3.2"
    const val Coil = "io.coil-kt:coil:$coilVersion"
    const val Compose = "io.coil-kt:coil-compose:$coilVersion"
}

object SquareUp {
    object Moshi {
        private const val moshiVersion = "1.12.0"
        const val Moshi = "com.squareup.moshi:moshi:$moshiVersion"
        const val CodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"
        const val Retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val MoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    }

    object OkHttp {
        private const val okhttpVersion = "5.0.0-alpha.2"
        const val Bom = "com.squareup.okhttp3:okhttp-bom:$okhttpVersion"
        const val OkHttp = "com.squareup.okhttp3:okhttp"
        const val LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"
    }
}

object Jetbrains {
    object Kotlin {
        const val StdLib = "org.jetbrains.kotlin:kotlin-stdlib:$KotlinVersion"
        const val GradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KotlinVersion"
        const val Reflect = "org.jetbrains.kotlin:kotlin-reflect:$KotlinVersion"
    }

    object KotlinX {
        object Coroutines {
            private const val coroutinesVersion = "1.5.2"
            const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val Android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val PlayServices =
                "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
        }
    }
}

object Facebook {
    object Android {
        const val Sdk = "com.facebook.android:facebook-android-sdk:12.0.1"
    }
}

object Venom {
    private const val venomVersion = "0.4.1"
    const val Debug = "com.github.YarikSOffice.Venom:venom:$venomVersion"
    const val NoOps = "com.github.YarikSOffice.Venom:venom-no-op:$venomVersion"
}

object Timber {
    const val Timber = "com.jakewharton.timber:timber:5.0.1"
}

object RgiCorp {
    const val GeoCords = "com.rgi-corp:ww-geo-coords:1.0"
}

object Microsoft {
    object SignalR {
        const val SignalR = "com.microsoft.signalr:signalr:5.0.11"
    }
}