plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id(Ktlint.Plugin) version Ktlint.Version
    id(Detekt.Plugin) version Detekt.Version
    id(Ksp.Plugin) version Ksp.Version
    id(Jetbrains.Dokka.Plugin) version KotlinVersion
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
            buildConfigField(
                "String",
                "SERVER_URL",
                "\"http://spaceapps.xyz\""
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "SERVER_URL",
                "\"http://spaceapps.xyz\""
            )
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
    hilt {
        enableAggregatingTask = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
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
    // Dagger-Hilt
    implementation(Google.Dagger.HiltAndroid)
    kapt(Google.Dagger.HiltAndroidCompiler)
    //    Retrofit
    implementation(SquareUp.Retrofit.Retrofit)
    implementation(SquareUp.Retrofit.MoshiConverter)
    //    OkHttp client
    implementation(platform(SquareUp.OkHttp.Bom))
    implementation(SquareUp.OkHttp.OkHttp)
    implementation(SquareUp.OkHttp.LoggingInterceptor)
    //    Timber logging
    implementation(Timber.Timber)
    //    Room database
    implementation(AndroidX.Room.Runtime)
    implementation(AndroidX.Room.Ktx)
    implementation(AndroidX.Room.Paging)
    ksp(AndroidX.Room.Compiler)
    //    Datastore
    implementation(AndroidX.DataStore.DataStore)
    implementation(AndroidX.DataStore.Preferences)
    //    SignalR
    implementation(Microsoft.SignalR.SignalR)
    //    Moshi
    implementation(SquareUp.Moshi.Moshi)
    ksp(SquareUp.Moshi.CodeGen)
    //    Paging
    implementation(AndroidX.Paging.Runtime)
    //    Coroutines
    implementation(platform(Jetbrains.KotlinX.Coroutines.Bom))
    implementation(Jetbrains.KotlinX.Coroutines.Core)
    implementation(Jetbrains.KotlinX.Coroutines.PlayServices)
    implementation(Jetbrains.KotlinX.Coroutines.Rx2)
    //    Firebase
    implementation(platform(Google.Firebase.Bom))
    implementation(Google.Firebase.Messaging)
    implementation(Google.Firebase.Installations)
    //    Google play services
    implementation(Google.Android.PlayServices.Location)

    //    Testing
    testImplementation(AndroidX.Test.Core)
    testImplementation(AndroidX.Test.Rules)
    testImplementation(Google.Truth.Truth)
    testImplementation(Mockito.Core)
    testImplementation(SquareUp.OkHttp.MockWebServer)
    testImplementation(Jetbrains.KotlinX.Coroutines.Test)
}
