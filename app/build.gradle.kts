import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val kotlinVersion by extra("1.5.0")
val composeVersion by extra("1.0.0-beta07")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("com.google.protobuf") version "0.8.15"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "com.spaceapps.myapplication"
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.spaceapps.myapplication.runner.SpaceAppsHiltRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
//        execution = "ANDROIDX_TEST_ORCHESTRATOR"
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
    val accompanistVersion = "0.10.0"
    implementation("com.google.accompanist:accompanist-coil:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
//    Coroutines
    val coroutinesVersion = "1.5.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

//    Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
//    Stetho
    val stethoVersion = "1.6.0"
    implementation("com.facebook.stetho:stetho:$stethoVersion")
    implementation("com.facebook.stetho:stetho-okhttp3:$stethoVersion")
//    OkHttp client
    val okhttpVersion = "5.0.0-alpha.2"
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
//    Timber logging
    implementation("com.jakewharton.timber:timber:4.7.1")
//    Google play services
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-auth:19.0.0")
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.maps.android:maps-ktx:3.0.1")
//    AndroidX
    implementation("androidx.core:core-ktx:1.6.0-beta01")
    implementation("androidx.appcompat:appcompat:1.4.0-alpha01")
    implementation("androidx.activity:activity-ktx:1.3.0-alpha08")
    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
    implementation("androidx.startup:startup-runtime:1.0.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.3.4")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha05")

//    Paging
    val pagingVersion = "3.0.0"
    val pagingComposeVersion = "1.0.0-alpha09"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingComposeVersion")
//    Moshi
    val moshiVersion = "1.12.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
//    WorkManager
    val workVersion = "2.5.0"
    implementation("androidx.work:work-runtime-ktx:$workVersion")
//    Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha01")
    runtimeOnly("androidx.compose.animation:animation:$composeVersion")
//    Dagger-Hilt
    val daggerVersion = "2.35.1"
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")
    val hiltVersion = "1.0.0"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha02")
    implementation("androidx.hilt:hilt-work:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")
//    Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-messaging-directboot")
//    Room database
    val roomVersion = "2.4.0-alpha02"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

//    Datastore
    val datastoreVersion = "1.0.0-beta01"
    implementation("androidx.datastore:datastore:$datastoreVersion")
    implementation("androidx.datastore:datastore-preferences:$datastoreVersion")
    implementation("com.google.protobuf:protobuf-javalite:3.11.1")

//    Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:9.1.1")

    val scarletVersion = "0.1.12"
    implementation("com.tinder.scarlet:scarlet:$scarletVersion")
    implementation("com.tinder.scarlet:message-adapter-moshi:$scarletVersion")
    implementation("com.tinder.scarlet:websocket-okhttp:$scarletVersion")
    implementation("com.tinder.scarlet:stream-adapter-coroutines:$scarletVersion")

//    Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.2")

//    Assertions
    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("androidx.test.ext:truth:1.3.0")
    testImplementation("com.google.truth:truth:1.1.2")

//   AndroidJUnitRunner and JUnit Rules
    testImplementation("androidx.test:runner:1.3.0")
    testImplementation("androidx.test:rules:1.3.0")
//    OkHttp MockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")
//    Mockito
    val mockitoVersion = "3.10.0"
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
//    Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
//    Instrumentation testing
    androidTestImplementation("androidx.test:core:1.3.0")

//   AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestUtil("androidx.test:orchestrator:1.3.0")

//    Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$daggerVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$daggerVersion")
//   Assertions
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.ext:truth:1.3.0")
    androidTestImplementation("com.google.truth:truth:1.1.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
//    Coroutines test
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
//    Venom
    val venomVersion = "0.3.1"
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
