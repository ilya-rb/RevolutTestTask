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

    api(Deps.Android.AndroidX.fragment)
    api(Deps.Android.AndroidX.material)
    api(Deps.Android.AndroidX.recyclerView)
    api(Deps.Android.AndroidX.emoji)
    api(Deps.Android.AndroidX.constraintLayout)
    api(Deps.Android.AndroidX.Lifecycle.ktx)
    api(Deps.Android.AndroidX.ViewModel.core)

    api(Deps.AdapterDelegates.core)
    api(Deps.AdapterDelegates.dsl)
}