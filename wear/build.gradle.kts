plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.spaceapps.myapplication"
        minSdk = 25
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
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
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ComposeVersion
    }
    packagingOptions {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
        }
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
    implementation(platform(Jetbrains.Kotlin.Bom))
    implementation(Jetbrains.Kotlin.StdLib)
    coreLibraryDesugaring(Android.Tools.Desugar)
    implementation(project(":core"))

    //    AndroidX
    implementation(AndroidX.StartUp.Runtime)

    //    Google Play services
    implementation(Google.Android.PlayServices.Wearable)
    //    Compose
    implementation(AndroidX.Wear.Compose.Material)
    implementation(AndroidX.Wear.Compose.Navigation)
    implementation(AndroidX.Wear.Compose.Foundation)
    implementation(AndroidX.Compose.Foundation)
    implementation(AndroidX.Compose.Ui)
    debugImplementation(AndroidX.Compose.UiTooling)
    implementation(AndroidX.Compose.Runtime)
    runtimeOnly(AndroidX.Compose.Animation)

    //    Firebase
    implementation(platform(Google.Firebase.Bom))
    implementation(Google.Firebase.Analytics)
    implementation(Google.Firebase.Crashlytics)
    implementation(Google.Firebase.Messaging)
    implementation(Google.Firebase.DynamicLinks)
    implementation(Google.Firebase.Installations)

    //    AppCompat
    implementation(AndroidX.AppCompat.AppCompat)
    implementation(AndroidX.Activity.Compose)

    //    Dagger-Hilt
    implementation(Google.Dagger.HiltAndroid)
    kapt(Google.Dagger.HiltAndroidCompiler)

    implementation(AndroidX.Hilt.ViewModel)
    implementation(AndroidX.Hilt.NavigationCompose)
    implementation(AndroidX.Hilt.Work)
    kapt(AndroidX.Hilt.Compiler)

    implementation(Timber.Timber)
    debugImplementation(Venom.Debug)
    releaseImplementation(Venom.NoOps)
}
