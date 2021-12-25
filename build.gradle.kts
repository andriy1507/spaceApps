buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Android.Tools.GradlePlugin)
        classpath(Jetbrains.Kotlin.GradlePlugin)
        classpath(Google.Services.GradlePlugin)
        classpath(Google.Firebase.CrashlyticsGradlePlugin)
        classpath(Google.Dagger.GradlePlugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}