import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val kotlinVersion = "1.4.31"
val composeVersion = "1.0.0-beta04"
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
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.spaceapps.myapplication"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.spaceapps.myapplication.runner.SpaceAppsHiltRunner"
        testInstrumentationRunnerArguments(mapOf("clearPackageData" to "true"))
    }
    buildTypes {
        getByName("release") {
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
        getByName("debug") {
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
        useIR = true
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    lint {
        isAbortOnError = false
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests.isReturnDefaultValues = true
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
    val accompanist_version = "0.6.2"
    implementation("dev.chrisbanes.accompanist:accompanist-coil:$accompanist_version")
    implementation("dev.chrisbanes.accompanist:accompanist-insets:$accompanist_version")
//    Coroutines
    val coroutines_version = "1.4.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
//    Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
//    Stetho
    val stetho_version = "1.6.0"
    implementation("com.facebook.stetho:stetho:$stetho_version")
    implementation("com.facebook.stetho:stetho-okhttp3:$stetho_version")
//    OkHttp client
    val okhttp_version = "5.0.0-alpha.2"
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
//    Timber logging
    implementation("com.jakewharton.timber:timber:4.7.1")
//    Google play services
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-auth:19.0.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
//    AndroidX
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.3.0-rc01")
    implementation("androidx.activity:activity-ktx:1.3.0-alpha06")
    implementation("androidx.activity:activity-compose:1.3.0-alpha06")
    implementation("androidx.fragment:fragment-ktx:1.3.2")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04")

//    Paging
    val paging_version = "3.0.0-beta03"
    val paging_compose_version = "1.0.0-alpha08"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_compose_version")
//    Moshi
    val moshi_version = "1.12.0"
    implementation("com.squareup.moshi:moshi:$moshi_version")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")
//    WorkManager
    val work_version = "2.5.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")
//    Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    runtimeOnly("androidx.compose.animation:animation:$composeVersion")
//    Navigation component
    val nav_version = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
//    Dagger-Hilt
    val dagger_version = "2.34-beta"
    implementation("com.google.dagger:hilt-android:$dagger_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_version")
    val hilt_version = "1.0.0-beta01"
    kapt("androidx.hilt:hilt-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-work:$hilt_version")
//    Firebase
    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-directboot")
//    Room database
    val room_version = "2.3.0-rc01"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

//    Datastore
    val datastore_version = "1.0.0-alpha08"
    implementation("androidx.datastore:datastore:$datastore_version")
    implementation("androidx.datastore:datastore-preferences:$datastore_version")
    implementation("com.google.protobuf:protobuf-javalite:3.15.1")

//    Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:9.1.1")

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
    testImplementation("com.squareup.okhttp3:mockwebserver:$okhttp_version")
//    Mockito
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("org.mockito:mockito-inline:3.9.0")
//    Instrumentation testing
    androidTestImplementation("androidx.test:core:1.3.0")

//   AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestUtil("androidx.test:orchestrator:1.3.0")

//    Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$dagger_version")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$dagger_version")
//   Assertions
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.ext:truth:1.3.0")
    androidTestImplementation("com.google.truth:truth:1.1.2")
    androidTestImplementation("com.google.truth:truth:1.1.2")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
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
