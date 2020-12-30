plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    kapt(Deps.Dagger.compiler)
    implementation(Deps.Dagger.core)

    implementation(Deps.Glide.core)
    implementation(Deps.Kotlin.std)
    implementation(Deps.RxJava.kotlin)
    implementation(Deps.Misc.lottie)

    api(Deps.Android.AndroidX.fragment)
    api(Deps.Android.AndroidX.material)
    api(Deps.Android.AndroidX.recyclerView)
    api(Deps.Android.AndroidX.constraintLayout)
    api(Deps.Android.AndroidX.lifecycleKtx)
    api(Deps.Android.AndroidX.viewModelCore)

    api(Deps.AdapterDelegates.core)
}