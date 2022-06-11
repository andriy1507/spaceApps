plugins {
    id(Jetbrains.Kover.Plugin) version Jetbrains.Kover.Version
    id(Jetbrains.Dokka.Plugin) version Jetbrains.Dokka.Version
}
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

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}
tasks.koverMergedHtmlReport {
    isEnabled = true
    htmlReportDir.set(layout.buildDirectory.dir("kover"))
}