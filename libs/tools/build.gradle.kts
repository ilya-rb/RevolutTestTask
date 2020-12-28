plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    kapt(Deps.Dagger.compiler)

    implementation(Deps.Dagger.core)
    implementation(Deps.Kotlin.std)
    implementation(Deps.RxJava.kotlin)
    implementation(Deps.RxJava.android)
    implementation(Deps.Misc.timber)
}