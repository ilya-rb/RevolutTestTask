plugins {
    id("com.android.application")
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
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
}