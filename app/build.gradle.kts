plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id(Ktlint.Plugin) version Ktlint.Version
    id(Detekt.Plugin) version Detekt.Version
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.spaceapps.myapplication"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjvm-default=all")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ComposeVersion
    }
    lint {
        abortOnError = false
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    packagingOptions {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    hilt {
        enableAggregatingTask = true
    }
}
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    disabledRules.set(setOf("no-wildcard-imports", "max-line-length", "import-ordering"))
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
detekt {
    config = files("$rootDir/.detekt/config.yml")
}
dependencies {
//    Kotlin
    implementation(platform(Jetbrains.Kotlin.Bom))
    implementation(Jetbrains.Kotlin.StdLib)
    coreLibraryDesugaring(Android.Tools.Desugar)
    implementation(project(":core"))
//    Accompanist
    implementation(Google.Accompanist.DrawablePainter)
    implementation(Google.Accompanist.Insets)
    implementation(Google.Accompanist.NavigationMaterial)
    implementation(Google.Accompanist.NavigationAnimation)
    implementation(Google.Accompanist.Pager)
    implementation(Google.Accompanist.PagerIndicators)
    implementation(Google.Accompanist.PlaceholderMaterial)
    implementation(Google.Accompanist.SwipeRefresh)
    implementation(Google.Accompanist.SystemUiController)
    implementation(Google.Accompanist.Permissions)

//    Coil
    implementation(Coil.Bom)
    implementation(Coil.Coil)
    implementation(Coil.Compose)
    implementation(Coil.Gif)
    implementation(Coil.Svg)
    implementation(Coil.Video)
//    Coroutines
    implementation(platform(Jetbrains.KotlinX.Coroutines.Bom))
    implementation(Jetbrains.KotlinX.Coroutines.Core)
    implementation(Jetbrains.KotlinX.Coroutines.Android)
    implementation(Jetbrains.KotlinX.Coroutines.PlayServices)
//    Timber logging
    implementation(Timber.Timber)
//    Google play services
    implementation(Google.Android.PlayServices.Location)
    implementation(Google.Android.PlayServices.Auth)
    implementation(Google.Android.PlayServices.Maps)
    implementation(Google.Android.PlayServices.Wallet)
    implementation(Google.Maps.Maps)
    implementation(Google.Maps.Utils)
//    AndroidX
    implementation(AndroidX.Core.Ktx)
    implementation(AndroidX.Emoji2.Emoji2)
    implementation(AndroidX.AppCompat.AppCompat)
    implementation(AndroidX.Activity.Ktx)
    implementation(AndroidX.Activity.Compose)
    implementation(AndroidX.StartUp.Runtime)
    implementation(AndroidX.Browser.Browser)
    implementation(AndroidX.Core.SplashScreen)
    implementation(AndroidX.Lifecycle.ViewModelCompose)
    implementation(AndroidX.Lifecycle.LiveData)
    implementation(AndroidX.Lifecycle.Runtime)

//    Paging
    implementation(AndroidX.Paging.Runtime)
    implementation(AndroidX.Paging.Compose)
//    WorkManager
    implementation(AndroidX.Work.Runtime)
//    Jetpack Compose
    implementation(AndroidX.Compose.Ui)
    implementation(AndroidX.Compose.UiUtil)
    implementation(AndroidX.Compose.LiveData)
    implementation(AndroidX.Compose.Material)
    implementation(AndroidX.Compose.Material3)
    implementation(AndroidX.Compose.Icons)
    implementation(AndroidX.Compose.Foundation)
    implementation(AndroidX.Navigation.Compose)
    implementation(AndroidX.Compose.Runtime)
    implementation(AndroidX.Compose.Compiler)
    runtimeOnly(AndroidX.Compose.Animation)
//    Dagger-Hilt
    implementation(Google.Dagger.HiltAndroid)
    kapt(Google.Dagger.HiltAndroidCompiler)

    implementation(AndroidX.Hilt.ViewModel)
    implementation(AndroidX.Hilt.NavigationCompose)
    implementation(AndroidX.Hilt.Work)
    kapt(AndroidX.Hilt.Compiler)
//    Firebase
    implementation(platform(Google.Firebase.Bom))
    implementation(Google.Firebase.Crashlytics)
    implementation(Google.Firebase.Analytics)
    implementation(Google.Firebase.Messaging)
    implementation(Google.Firebase.Installations)
    implementation(Google.Firebase.DynamicLinks)

//    Facebook SDK
    implementation(Facebook.Android.Sdk)

//    WW-geo
    implementation(RgiCorp.GeoCords)

//    SignalR
    implementation(Microsoft.SignalR.SignalR)
//    CameraX
    implementation(AndroidX.CameraX.Core)
    implementation(AndroidX.CameraX.Camera2)
    implementation(AndroidX.CameraX.Lifecycle)
    implementation(AndroidX.CameraX.View)

    implementation(AndroidX.Media3.Ui)
    implementation(AndroidX.Media3.ExoPlayer)
    implementation(AndroidX.Media3.Sessions)

//    Venom
    debugImplementation(Venom.Debug)
    releaseImplementation(Venom.NoOps)

    debugImplementation(AndroidX.Compose.UiTooling)
    debugImplementation(Jetbrains.Kotlin.Reflect)

//    Test
    androidTestImplementation(AndroidX.Test.Core)
    androidTestImplementation(AndroidX.Test.Runner)
    androidTestImplementation(AndroidX.Test.Rules)
    androidTestImplementation(AndroidX.Compose.Test.JUnit4)
    androidTestImplementation(AndroidX.Compose.Test.Test)
    debugImplementation(AndroidX.Compose.Test.Manifest)
    androidTestImplementation(Google.Truth.Truth)
}
