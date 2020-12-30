import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("de.mannodermaus.android-junit5")
    id("com.google.firebase.crashlytics")
    kotlin("android")
    kotlin("kapt")
}

android {
    defaultConfig {
        applicationId = "com.illiarb.revoluttest"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        all {
            val config = file("$rootDir/api-config.properties")
            val properties = Properties().also {
                it.load(FileInputStream(config))
            }
            buildConfigField("String", "API_URL", properties.getProperty("api.url"))
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
    }
}

dependencies {
    kapt(Deps.Dagger.compiler)
    implementation(Deps.Dagger.core)

    kapt(Deps.Moshi.codegen)
    implementation(Deps.Moshi.kotlin)
    implementation(Deps.Moshi.adapters)

    implementation(project(Deps.Modules.Core.ui))
    implementation(project(Deps.Modules.Core.tools))
    implementation(project(Deps.Modules.Core.util))

    implementation(Deps.Kotlin.std)

    implementation(Deps.Android.AndroidX.Room.core)

    implementation(Deps.RxJava.kotlin)
    implementation(Deps.RxJava.android)
    implementation(Deps.RxJava.bindings)
    implementation(Deps.RxJava.relay)

    implementation(Deps.Retrofit.core)
    implementation(Deps.Retrofit.converterMoshi)
    implementation(Deps.Retrofit.rxJavaAdapter)

    implementation(Deps.Misc.timber)
    implementation(Deps.Misc.viewBindingPropertyDelegate)
    implementation(Deps.Misc.binaryPrefs)

    implementation(platform(Deps.Firebase.bomPlatform))
    implementation(Deps.Firebase.crashlytics)

    debugImplementation(Deps.Tools.Debug.LeakCanary.android)

    debugImplementation(Deps.Tools.Debug.Flipper.flipper)
    debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)
    debugImplementation(Deps.Tools.Debug.Flipper.soLoader)

    testRuntimeOnly(Deps.Test.JUnit5.jupiterEngine)

    testImplementation(Deps.Test.AndroidX.core)
    testImplementation(Deps.Test.AndroidX.rules)
    testImplementation(Deps.Test.AndroidX.extJunit)
    testImplementation(Deps.Test.JUnit5.jupiterApi)
    testImplementation(Deps.Test.JUnit5.jupiterParams)
    testImplementation(Deps.Test.truth)
    testImplementation(Deps.Test.junit)

    androidTestImplementation(Deps.Test.JUnit5.jupiterApi)
    androidTestImplementation(Deps.Test.JUnit5.androidTestCore)
    androidTestImplementation(Deps.Test.AndroidX.runner)

    androidTestRuntimeOnly(Deps.Test.JUnit5.androidTestRunner)
}