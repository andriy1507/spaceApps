plugins {
    id("com.android.library")
    kotlin("android")
    id(Ktlint.Plugin) version Ktlint.Version
    id(Detekt.Plugin) version Detekt.Version
    id(Jetbrains.Dokka.Plugin) version Jetbrains.Dokka.Version
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    //    Timber logging
    implementation(Timber.Timber)
    //    AndroidX
    implementation(AndroidX.Core.Ktx)
    //    Jetpack Compose
    implementation(AndroidX.Compose.Ui)
    implementation(AndroidX.Compose.UiUtil)
    implementation(AndroidX.Compose.Material)
    implementation(AndroidX.Compose.Material3)
    implementation(AndroidX.Compose.Runtime)
    implementation(AndroidX.Compose.Compiler)
    implementation(AndroidX.Compose.Foundation)
    //    Accompanist
    implementation(Google.Accompanist.Insets)
}
