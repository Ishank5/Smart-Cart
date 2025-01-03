// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Declaring Android Application plugin (apply false ensures it's not automatically applied here)
    alias(libs.plugins.android.application) apply false

    // Declaring Kotlin Android plugin (apply false ensures it's not automatically applied here)
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Declaring Firebase Google Services plugin
    id("com.google.gms.google-services") version "4.4.2" apply false // Using compatible Google Services version
}

buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Central Maven repository
    }
    dependencies {
        // Include Google services classpath
        classpath("com.google.gms:google-services:4.4.2") // Version of Google services plugin
    }
}
