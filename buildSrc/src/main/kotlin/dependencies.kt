@file:Suppress("unused")

object Build {
    const val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:1.0.9"

    val kotlinStandardFreeCompilerArgs = listOf(
        "-Xinline-classes",
        "-Xopt-in=kotlin.RequiresOptIn",
        // Generate nullability assertions for non-null Java expressions
        "-Xstrict-java-nullability-assertions"
    )

    val daggerJavaCompilerArgs = listOf(
        "-Adagger.formatGeneratedSource=disabled",
        "-Adagger.gradle.incremental=enabled"
    )
}

object Deps {

    object GradlePlugins {
        const val jacoco = "org.jacoco:org.jacoco.core:0.8.6"
        const val versionsCheck = "com.github.ben-manes:gradle-versions-plugin:0.33.0"
        const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.7.0.0"
        const val googleServices = "com.google.gms:google-services:4.3.4"
        const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.4.1"
    }

    object Modules {

        const val app = ":app"

        object Core {
            const val ui = ":libs:ui"
            const val tools = ":libs:tools"
            const val util = ":libs:util"
        }
    }

    object Kotlin {
        @Suppress("MemberVisibilityCanBePrivate")
        // false positive
        const val kotlinVersion = "1.4.10"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    }

    object Firebase {
        const val bomPlatform = "com.google.firebase:firebase-bom:26.2.0"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Android {

        object Build {
            const val compileSdkVersion = 30
            const val targetSdkVersion = 30
            const val minSdkVersion = 21
            const val gradlePlugin = "com.android.tools.build:gradle:4.1.1"
        }

        object AndroidX {
            private const val archComponentsVersion = "2.3.0-rc01"

            object Room {
                private const val roomVersion = "2.3.0-alpha04"

                const val core = "androidx.room:room-ktx:$roomVersion"
                const val compiler = "androidx.room:room-compiler:$roomVersion"
            }

            const val viewModelCore = "androidx.lifecycle:lifecycle-viewmodel-ktx:$archComponentsVersion"
            const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$archComponentsVersion"
            const val fragment = "androidx.fragment:fragment-ktx:1.3.0-rc01"
            const val material = "com.google.android.material:material:1.3.0-beta01"
            const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-beta01"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0-alpha2"
        }
    }

    object Glide {
        private const val glideVersion = "4.11.0"

        const val core = "com.github.bumptech.glide:glide:$glideVersion"
    }

    object RxJava {
        const val kotlin = "io.reactivex.rxjava3:rxkotlin:3.0.1"
        const val android = "io.reactivex.rxjava3:rxandroid:3.0.0"
        const val bindings = "com.jakewharton.rxbinding4:rxbinding:4.0.0"
        const val relay = "com.jakewharton.rxrelay3:rxrelay:3.0.0"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"

        const val core = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val okHttp = "com.squareup.okhttp3:okhttp:4.10.0-RC1"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
        const val rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
    }

    object Moshi {
        private const val moshiVersion = "1.11.0"

        const val kotlin = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
        const val adapters = "com.squareup.moshi:moshi-adapters:$moshiVersion"
    }

    object AdapterDelegates {
        private const val adapterDelegatesVersion = "4.3.0"

        const val core = "com.hannesdorfmann:adapterdelegates4:$adapterDelegatesVersion"
    }

    object Dagger {
        private const val daggerVersion = "2.30.1"

        const val core = "com.google.dagger:dagger:$daggerVersion"
        const val compiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    }

    object Tools {

        object Debug {

            object Flipper {
                private const val flipperVersion = "0.63.0"

                const val flipper = "com.facebook.flipper:flipper:$flipperVersion"
                const val flipperNetwork =
                    "com.facebook.flipper:flipper-network-plugin:$flipperVersion"
                const val soLoader = "com.facebook.soloader:soloader:0.9.0"
            }

            object LeakCanary {
                private const val leakCanaryVersion = "2.5"

                const val android = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
            }
        }
    }

    object Misc {
        const val timber = "com.jakewharton.timber:timber:4.7.1"
        const val javax = "javax.inject:javax.inject:1"
        const val lottie = "com.airbnb.android:lottie:3.0.7"
        const val binaryPrefs = "com.github.yandextaxitech:binaryprefs:1.0.1"
        const val viewBindingPropertyDelegate =
            "com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:1.3.1"
    }

    object Test {

        const val junit = "junit:junit:4.13.1"
        const val truth = "com.google.truth:truth:1.1"
        const val kaspresso = "com.kaspersky.android-components:kaspresso:1.2.0"
        const val kakao = "com.agoda.kakao:kakao:2.3.4"
        const val mockk = "io.mockk:mockk:1.10.2"

        object JUnit5 {
            private const val jUnitVersion = "5.7.0"
            private const val androidTestVersion = "1.2.0"

            const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$jUnitVersion"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$jUnitVersion"
            const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$jUnitVersion"
            const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:$jUnitVersion"

            // TODO: Use when it upgrades to AndroidX
            const val androidTestCore =
                "de.mannodermaus.junit5:android-test-core:$androidTestVersion"
            const val androidTestRunner =
                "de.mannodermaus.junit5:android-test-runner:$androidTestVersion"
        }

        object AndroidX {
            private const val espressoVersion = "3.4.0-alpha02"

            const val core = "androidx.arch.core:core-testing:2.1.0"
            const val rules = "androidx.test:rules:1.3.1-alpha02"
            const val runner = "androidx.test:runner:1.3.1-alpha02"
            const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
            const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"
            const val extJunit = "androidx.test.ext:junit-ktx:1.1.3-alpha02"
            const val uiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
            const val benchmark = "androidx.benchmark:benchmark-junit4:1.1.0-alpha01"
        }
    }
}