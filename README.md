# Revolut test task
Release APK can be found [here](https://github.com/ilya-rb/RevolutTestTask/releases/download/v1.1/app-release-1.1.apk)

[![CircleCI](https://circleci.com/gh/ilya-rb/RevolutTestTask/tree/main.svg?style=svg)](https://circleci.com/gh/ilya-rb/RevolutTestTask/tree/main)
[![codecov](https://codecov.io/gh/ilya-rb/RevolutTestTask/branch/main/graph/badge.svg?token=TT37OD91DA)](https://codecov.io/gh/ilya-rb/RevolutTestTask)

List all currencies you get from the endpoint (one per row). Each row has an input where
you can enter any amount of money. When you tap on a currency row it should slide to
the top and it's input becomes the first responder. When you’re changing the amount
the app must simultaneously update the corresponding value for other currencies.
Use any libraries and languages(java/kotlin) you want. Please, note that the solution
should be ​production ready. ​(Please use the public stable version of Android studio, if
you’re using this IDE for development).

## Tech stack

- Kotlin
- RxJava3
- Dagger
- AndroidX libraries (Material, ViewModel, RecyclerView, etc..)
- Glide
- Retrofit (Moshi for deserialization)
- [Adapter delegates](https://github.com/sockeqwe/AdapterDelegates) for RecyclerView 
- [Binary preferences](https://github.com/yandextaxitech/binaryprefs) as a file cache
- LeakCanary and Flipper for debugging
- JUnit5 for unit and integration tests
- Jacoco for tests coverage
- Detekt for static analysis
- CircleCI as a CI server
- Firebase crashlytics
- Timber for logging
- a small bunch of functional tests with Kaspresso

## Additional things included

- Dark theme support
- Offline mode (caching and auto reconnecting when network available)
- Settings connectivity panel (from Android Q)
- Edge to edge support

## Screenshots
![1](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/1.jpeg)
![2](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/2.jpeg)
![6](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/6.jpeg)
![3](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/3.jpeg)
![4](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/4.jpeg)
![5](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/5.jpeg)
![7](https://github.com/ilya-rb/RevolutTestTask/blob/main/art/7.jpeg)