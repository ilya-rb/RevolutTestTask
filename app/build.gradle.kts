import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("de.mannodermaus.android-junit5")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
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

    signingConfigs {
        val propsFile = rootProject.file("keystore.properties")
        if (propsFile.exists()) {
            val props = Properties().also { it.load(FileInputStream(propsFile)) }

            create("release") {
                storeFile = file(props.getProperty("storeFile"))
                storePassword = props.getProperty("storePassword")
                keyAlias = props.getProperty("keyAlias")
                keyPassword = props.getProperty("keyPassword")
            }
        } else {
            create("release").initWith(getByName("debug"))
        }
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
                isDebuggable = false
                isMinifyEnabled = true
                isShrinkResources = true
                signingConfig = signingConfigs.getByName("release")
                proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
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
    testImplementation(Deps.Test.mockk)

    androidTestImplementation(Deps.Test.JUnit5.jupiterApi)
    androidTestImplementation(Deps.Test.JUnit5.androidTestCore)
    androidTestImplementation(Deps.Test.AndroidX.runner)
    androidTestImplementation(Deps.Test.AndroidX.extJunit)
    androidTestImplementation(Deps.Test.kaspresso)
    androidTestImplementation(Deps.Test.kakao)
    androidTestImplementation(Deps.Test.AndroidX.uiAutomator)

    androidTestRuntimeOnly(Deps.Test.JUnit5.androidTestRunner)
    androidTestRuntimeOnly(Deps.Test.JUnit5.junitVintageEngine)
}