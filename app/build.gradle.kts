import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val kotlinVersion by extra("1.5.21")
val composeVersion by extra("1.0.3")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.protobuf") version "0.8.15"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.spaceapps.myapplication"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.spaceapps.myapplication.runner.SpaceAppsHiltRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            buildConfigField(
                "String",
                "SERVER_URL",
                "\"https://develop-space-apps-backend.herokuapp.com\""
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
                "\"https://develop-space-apps-backend.herokuapp.com\""
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    lint {
        isAbortOnError = false
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                "META-INF/license.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/LGPL2.1",
                "META-INF/AL2.0"
            )
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
//    Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
//    Accompanist
    val accompanistVersion = "0.21.0-beta"
    implementation("com.google.accompanist:accompanist-drawablepainter:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

//    Coil
    val coilVersion = "1.3.2"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
//    Coroutines
    val coroutinesVersion = "1.5.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

//    Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
//    OkHttp client
    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.2"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
//    Timber logging
    implementation("com.jakewharton.timber:timber:5.0.1")
//    Google play services
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-auth:19.2.0")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.maps.android:maps-ktx:3.2.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.2.0")
//    AndroidX
    val lifecycleVersion = "2.4.0"
    val activityVersion = "1.4.0"
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0-rc01")
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")
    implementation("androidx.startup:startup-runtime:1.1.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.core:core-splashscreen:1.0.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

//    Paging
    val pagingVersion = "3.0.1"
    val pagingComposeVersion = "1.0.0-alpha13"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingComposeVersion")
//    Moshi
    val moshiVersion = "1.12.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
//    WorkManager
    val workVersion = "2.7.0"
    implementation("androidx.work:work-runtime-ktx:$workVersion")
//    Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha01")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-beta01")
    runtimeOnly("androidx.compose.animation:animation:$composeVersion")
//    Dagger-Hilt
    val daggerVersion = "2.39.1"
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")
    val hiltVersion = "1.0.0"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-work:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")
//    Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.4.2"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-installations")
    implementation("com.google.firebase:firebase-dynamic-links-ktx")
//    Room database
    val roomVersion = "2.4.0-beta01"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

//    Datastore
    val datastoreVersion = "1.0.0"
    implementation("androidx.datastore:datastore:$datastoreVersion")
    implementation("androidx.datastore:datastore-preferences:$datastoreVersion")
    implementation("com.google.protobuf:protobuf-javalite:3.11.1")

//    Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:12.0.1")

//    WW-geo
    implementation("com.rgi-corp:ww-geo-coords:1.0")

//    SignalR
    implementation("com.microsoft.signalr:signalr:5.0.11")
    implementation("org.slf4j:slf4j-jdk14:1.7.25")

//    CameraX
    val cameraxVersion = "1.0.2"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:1.0.0-alpha30")

//    Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")

//    Assertions
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("androidx.test.ext:truth:1.4.0")
    testImplementation("com.google.truth:truth:1.1.3")

//   AndroidJUnitRunner and JUnit Rules
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")
//    OkHttp MockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver")
//    Mockito
    val mockitoVersion = "4.0.0"
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
//    Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
//    Instrumentation testing
    androidTestImplementation("androidx.test:core:1.4.0")

//   AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestUtil("androidx.test:orchestrator:1.4.0")

//    Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$daggerVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$daggerVersion")
//   Assertions
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:truth:1.4.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
//    Coroutines test
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
//    Venom
    val venomVersion = "0.4.1"
    debugImplementation("com.github.YarikSOffice.Venom:venom:$venomVersion")
    releaseImplementation("com.github.YarikSOffice.Venom:venom-no-op:$venomVersion")

    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}

// Generates the java Protobuf-lite code for the Protobufs in this project.
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins.create("java") {
                option("lite")
            }
        }
    }
}
