const val KotlinVersion = "1.6.10"
const val ComposeVersion = "1.1.0"

object AndroidX {
    object Wear {
        object Compose {
            private const val Version = "1.0.0-alpha15"
            const val Material = "androidx.wear.compose:compose-material:$Version"
            const val Foundation = "androidx.wear.compose:compose-foundation:$Version"
            const val Navigation = "androidx.wear.compose:compose-navigation:$Version"
        }
    }

    object Hilt {
        private const val HiltVersion = "1.0.0"
        const val ViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val NavigationCompose = "androidx.hilt:hilt-navigation-compose:$HiltVersion"
        const val Work = "androidx.hilt:hilt-work:$HiltVersion"
        const val Compiler = "androidx.hilt:hilt-compiler:$HiltVersion"
    }

    object Compose {
        const val UiTooling = "androidx.compose.ui:ui-tooling:$ComposeVersion"
        const val UiUtil = "androidx.compose.ui:ui-util:$ComposeVersion"
        const val Ui = "androidx.compose.ui:ui:$ComposeVersion"
        const val Compiler = "androidx.compose.compiler:compiler:$ComposeVersion"
        const val LiveData = "androidx.compose.runtime:runtime-livedata:$ComposeVersion"
        const val Runtime = "androidx.compose.runtime:runtime:$ComposeVersion"
        const val Material = "androidx.compose.material:material:$ComposeVersion"
        const val Material3 = "androidx.compose.material3:material3:1.0.0-alpha04"
        const val Icons = "androidx.compose.material:material-icons-extended:$ComposeVersion"
        const val Foundation = "androidx.compose.foundation:foundation:$ComposeVersion"
        const val Animation = "androidx.compose.animation:animation:$ComposeVersion"

        object Test {
            const val Test = "androidx.compose.ui:ui-test:$ComposeVersion"
            const val JUnit4 = "androidx.compose.ui:ui-test-junit4:$ComposeVersion"
            const val Manifest = "androidx.compose.ui:ui-test-manifest:$ComposeVersion"
        }
    }

    object ConstraintLayout {
        private const val Version = "1.0.0"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:$Version"
    }

    object Navigation {
        const val Compose = "androidx.navigation:navigation-compose:2.4.0-rc01"
    }

    object Room {
        private const val RoomVersion = "2.4.1"
        const val Runtime = "androidx.room:room-runtime:$RoomVersion"
        const val Ktx = "androidx.room:room-ktx:$RoomVersion"
        const val Paging = "androidx.room:room-paging:$RoomVersion"
        const val Compiler = "androidx.room:room-compiler:$RoomVersion"
    }

    object Emoji2 {
        private const val Emoji2Version = "1.0.1"
        const val Emoji2 = "androidx.emoji2:emoji2:$Emoji2Version"
    }

    object DataStore {
        private const val DatastoreVersion = "1.0.0"
        const val DataStore = "androidx.datastore:datastore:$DatastoreVersion"
        const val Preferences = "androidx.datastore:datastore-preferences:$DatastoreVersion"
    }

    object CameraX {
        private const val CameraxVersion = "1.0.2"
        const val Core = "androidx.camera:camera-core:$CameraxVersion"
        const val Camera2 = "androidx.camera:camera-camera2:$CameraxVersion"
        const val Lifecycle = "androidx.camera:camera-lifecycle:$CameraxVersion"
        const val View = "androidx.camera:camera-view:1.1.0-beta01"
    }

    object Paging {
        const val Runtime = "androidx.paging:paging-runtime-ktx:3.1.0"
        const val Compose = "androidx.paging:paging-compose:1.0.0-alpha14"
    }

    object Work {
        const val Runtime = "androidx.work:work-runtime-ktx:2.7.1"
    }

    object Core {
        const val Ktx = "androidx.core:core-ktx:1.7.0"
        const val SplashScreen = "androidx.core:core-splashscreen:1.0.0-beta01"
    }

    object AppCompat {
        const val AppCompat = "androidx.appcompat:appcompat:1.4.1"
    }

    object Activity {
        private const val ActivityVersion = "1.4.0"
        const val Ktx = "androidx.activity:activity-ktx:$ActivityVersion"
        const val Compose = "androidx.activity:activity-compose:$ActivityVersion"
    }

    object StartUp {
        const val Runtime = "androidx.startup:startup-runtime:1.1.0"
    }

    object Browser {
        const val Browser = "androidx.browser:browser:1.4.0"
    }

    object Lifecycle {
        private const val LifecycleVersion = "2.4.0"
        const val ViewModelCompose =
            "androidx.lifecycle:lifecycle-viewmodel-compose:$LifecycleVersion"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$LifecycleVersion"
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$LifecycleVersion"
    }

    object Media3 {
        private const val Media3Version = "1.0.0-alpha01"
        const val Ui = "androidx.media3:media3-ui:$Media3Version"
        const val Sessions = "androidx.media3:media3-session:$Media3Version"
        const val ExoPlayer = "androidx.media3:media3-exoplayer:$Media3Version"
    }

    object Test {
        private const val TestVersion = "1.4.0"
        const val Core = "androidx.test:core-ktx:$TestVersion"
        const val Runner = "androidx.test:runner:$TestVersion"
        const val Rules = "androidx.test:rules:$TestVersion"
    }
}

object Android {
    object Tools {
        const val Desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
        const val GradlePlugin = "com.android.tools.build:gradle:7.1.1"
    }
}

object Google {
    object Android {
        object PlayServices {
            const val Wearable = "com.google.android.gms:play-services-wearable:17.1.0"
            const val Location = "com.google.android.gms:play-services-location:19.0.0"
            const val Auth = "com.google.android.gms:play-services-auth:20.0.0"
            const val Maps = "com.google.android.gms:play-services-maps:18.0.1"
            const val Wallet = "com.google.android.gms:play-services-wallet:19.0.0"
        }
    }

    object Accompanist {
        private const val AccompanistVersion = "0.22.1-rc"
        const val DrawablePainter =
            "com.google.accompanist:accompanist-drawablepainter:$AccompanistVersion"
        const val Insets = "com.google.accompanist:accompanist-insets:$AccompanistVersion"
        const val NavigationMaterial =
            "com.google.accompanist:accompanist-navigation-material:$AccompanistVersion"
        const val NavigationAnimation =
            "com.google.accompanist:accompanist-navigation-animation:$AccompanistVersion"
        const val Pager = "com.google.accompanist:accompanist-pager:$AccompanistVersion"
        const val PagerIndicators =
            "com.google.accompanist:accompanist-pager-indicators:$AccompanistVersion"
        const val PlaceholderMaterial =
            "com.google.accompanist:accompanist-placeholder-material:$AccompanistVersion"
        const val SwipeRefresh =
            "com.google.accompanist:accompanist-swiperefresh:$AccompanistVersion"
        const val SystemUiController =
            "com.google.accompanist:accompanist-systemuicontroller:$AccompanistVersion"
        const val Permissions = "com.google.accompanist:accompanist-permissions:$AccompanistVersion"
    }

    object Dagger {
        private const val DaggerVersion = "2.40.5"
        const val HiltAndroid = "com.google.dagger:hilt-android:$DaggerVersion"
        const val Testing = "com.google.dagger:hilt-android-testing:$DaggerVersion"
        const val HiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$DaggerVersion"
        const val GradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$DaggerVersion"
    }

    object Services {
        const val GradlePlugin = "com.google.gms:google-services:4.3.10"
    }

    object Firebase {
        const val CrashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:2.8.1"
        const val Bom = "com.google.firebase:firebase-bom:29.0.4"
        const val Crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val Analytics = "com.google.firebase:firebase-analytics-ktx"
        const val Messaging = "com.google.firebase:firebase-messaging-ktx"
        const val Installations = "com.google.firebase:firebase-installations"
        const val DynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
    }

    object Maps {
        private const val MapsVersion = "3.3.0"
        const val Maps = "com.google.maps.android:maps-ktx:$MapsVersion"
        const val Utils = "com.google.maps.android:maps-utils-ktx:$MapsVersion"
        const val Compose = "com.google.maps.android:maps-compose:1.0.0"
    }
    object Truth {
        private const val TruthVersion = "1.1.3"
        const val Truth = "com.google.truth:truth:$TruthVersion"
    }
}

object Coil {
    private const val CoilVersion = "2.0.0-alpha07"
    const val Bom = "io.coil-kt:coil-bom:$CoilVersion"
    const val Coil = "io.coil-kt:coil"
    const val Compose = "io.coil-kt:coil-compose"
    const val Gif = "io.coil-kt:coil-gif"
    const val Svg = "io.coil-kt:coil-svg"
    const val Video = "io.coil-kt:coil-video"
}

object SquareUp {
    object Moshi {
        private const val MoshiVersion = "1.13.0"
        const val Moshi = "com.squareup.moshi:moshi:$MoshiVersion"
        const val CodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$MoshiVersion"
    }

    object Retrofit {
        private const val RetrofitVersion = "2.9.0"
        const val Retrofit = "com.squareup.retrofit2:retrofit:$RetrofitVersion"
        const val MoshiConverter = "com.squareup.retrofit2:converter-moshi:$RetrofitVersion"
    }

    object OkHttp {
        private const val OkHttpVersion = "5.0.0-alpha.4"
        const val Bom = "com.squareup.okhttp3:okhttp-bom:$OkHttpVersion"
        const val OkHttp = "com.squareup.okhttp3:okhttp"
        const val LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"
        const val MockWebServer = "com.squareup.okhttp3:mockwebserver"
    }
}

object Mockito {
    private const val MockitoVersion = "4.3.1"
    const val Core = "org.mockito:mockito-core:$MockitoVersion"
}

object Jetbrains {
    object Dokka {
        const val Plugin = "org.jetbrains.dokka"
    }

    object Kover {
        const val Version = "0.5.0"
        const val Plugin = "org.jetbrains.kotlinx.kover"
    }

    object Kotlin {
        const val GradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KotlinVersion"
        const val Bom = "org.jetbrains.kotlin:kotlin-bom:$KotlinVersion"
        const val StdLib = "org.jetbrains.kotlin:kotlin-stdlib"
        const val Reflect = "org.jetbrains.kotlin:kotlin-reflect"
    }

    object KotlinX {
        object Coroutines {
            private const val CoroutinesVersion = "1.6.0"
            const val Bom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:$CoroutinesVersion"
            const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
            const val Android = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
            const val PlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"
            const val Rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2"
            const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test"
        }
    }
}

object Facebook {
    object Android {
        const val Sdk = "com.facebook.android:facebook-android-sdk:12.3.0"
    }
}

object Venom {
    private const val VenomVersion = "0.5.0"
    const val Debug = "com.github.YarikSOffice.Venom:venom:$VenomVersion"
    const val NoOps = "com.github.YarikSOffice.Venom:venom-no-op:$VenomVersion"
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

object Grpc {
    private const val GrpcVersion = "1.42.1"
    const val Bom = "io.grpc:grpc-bom:$GrpcVersion"
    const val Gen = "io.grpc:protoc-gen-grpc-kotlin:1.2.0"
    const val Auth = "io.grpc:grpc-auth:1.42.1"
    const val OkHttp = "io.grpc:grpc-okhttp:1.42.1"
    const val Android = "io.grpc:grpc-android:1.42.1"
    const val KotlinStub = "io.grpc:grpc-kotlin-stub:1.2.0"
    const val KotlinStubLite = "io.grpc:grpc-kotlin-stub-lite:1.0.0"
    const val ProtobufNano = "io.grpc:grpc-protobuf-nano:1.21.1"
    const val ProtobufLite = "io.grpc:grpc-protobuf-lite:1.42.1"
}

object Ktlint {
    const val Version = "10.2.1"
    const val Plugin = "org.jlleitschuh.gradle.ktlint"
}

object Detekt {
    const val Version = "1.19.0"
    const val Plugin = "io.gitlab.arturbosch.detekt"
}

object Ksp {
    const val Version = "1.6.10-1.0.2"
    const val Plugin = "com.google.devtools.ksp"
}