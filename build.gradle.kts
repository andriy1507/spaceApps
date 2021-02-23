buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha07")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
        classpath("com.google.gms:google-services:4.3.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.32-alpha")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}