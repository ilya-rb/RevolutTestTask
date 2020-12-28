import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.closureOf
import groovy.lang.Closure

apply {
    plugin("com.github.ben-manes.versions")
}

buildscript {

    repositories {
        google()
        jcenter {
            content {
                // just allow to include kotlinx projects
                // detekt needs 'kotlinx-html' for the html report
                includeGroup("org.jetbrains.kotlinx")
            }
        }
    }

    dependencies {
        classpath(Deps.Android.Build.gradlePlugin)
        classpath(Deps.Kotlin.gradlePlugin)
        classpath(Deps.GradlePlugins.versionsCheck)
        classpath(Deps.GradlePlugins.jacoco)
        classpath(Deps.GradlePlugins.junit5)
        classpath(Deps.GradlePlugins.jetifierCheck)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("com.github.ben-manes.versions") version "0.36.0"
    id("com.github.plnice.canidropjetifier") version "0.5"
}

allprojects {

    repositories {
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        jcenter()
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            when {
                requested.name.startsWith("kotlin-stdlib") -> {
                    useTarget(
                        "${requested.group}:${requested.name.replace("jre", "jdk")}:${requested.version}"
                    )
                }
                else -> when (requested.group) {
                    "org.jetbrains.kotlin" -> useVersion(Deps.Kotlin.kotlinVersion)
                }
            }
        }
    }
}

subprojects {
    apply {
        from(rootProject.file("code-quality-tools/detekt.gradle"))
    }

    configureAndroid()
    configureKotlin()
    configureJava()
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    this.jvmTarget = "1.8"
}

tasks.register(name = "clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

fun Project.configureJava() {
    tasks.withType<JavaCompile>().all {
        options.compilerArgs.addAll(Build.daggerJavaCompilerArgs)
    }

    plugins.withType<JavaBasePlugin> {
        extensions.getByType<JavaPluginExtension>().apply {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            @Suppress("SuspiciousCollectionReassignment")
            freeCompilerArgs += Build.kotlinStandardFreeCompilerArgs
        }
    }

    plugins.withId("org.jetbrains.kotlin.kapt") {
        extensions.getByType<KaptExtension>().apply {
            correctErrorTypes = true
            mapDiagnosticLocations = true

            arguments {
                arg("dagger.formatGeneratedSource", "disabled")
                arg("dagger.experimentalDaggerErrorMessages", "enabled")
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", true)
            }
        }
    }
}

fun Project.configureAndroid() {
    plugins.matching { it is AppPlugin || it is LibraryPlugin }.whenPluginAdded {
        configure<BaseExtension> {
            // TODO: Add this later
            // apply(from = file("$rootDir/code-quality-tools/jacoco.gradle"))

            setCompileSdkVersion(Deps.Android.Build.compileSdkVersion)

            defaultConfig {
                minSdkVersion(Deps.Android.Build.minSdkVersion)
                targetSdkVersion(Deps.Android.Build.targetSdkVersion)
                versionCode = 1
                versionName = "1.0"
                vectorDrawables.useSupportLibrary = true
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
                isCoreLibraryDesugaringEnabled = true
            }

            configureTestOptions()

            configureAndroidLint(this)

            packagingOptions {
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/LICENSE")
                exclude("META-INF/LICENSE.txt")
                exclude("META-INF/LICENSE.md")
                exclude("META-INF/license.txt")
                exclude("META-INF/NOTICE")
                exclude("META-INF/LICENSE-notice.md")
                exclude("META-INF/NOTICE.txt")
                exclude("META-INF/notice.txt")
                exclude("META-INF/AL2.0")
                exclude("META-INF/LGPL2.1")
                exclude("META-INF/licenses/**")
                exclude("META-INF/*.kotlin_module")
                exclude("**/attach_hotspot_windows.dll")
            }

            dependencies.add("coreLibraryDesugaring", Build.coreLibraryDesugaring)
        }
    }

    plugins.withType<LibraryPlugin> {
        configure<LibraryExtension> {
            // Disable build config generation for libraries
            libraryVariants.all {
                generateBuildConfigProvider?.orNull?.let {
                    it.enabled = false
                }
            }
        }
    }
}

fun Project.configureAndroidLint(androidExtension: BaseExtension) {
    androidExtension.lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = false
        isCheckAllWarnings = true
        isShowAll = true
        isExplainIssues = true
        lintConfig = rootProject.file("lint.xml")
        xmlReport = false
        htmlOutput = file("reports/${project.name}_lint_report.html")

        // App does not have deep linking
        disable("GoogleAppIndexingWarning")
        // Okio references java.nio that does not presented in Android SDK
        disable("InvalidPackage")
        // View binding issues for unused resources and ids
        disable("UnusedIds")
    }
}

fun BaseExtension.configureTestOptions() {
    @Suppress("UNCHECKED_CAST")
    testOptions.unitTests.all(closureOf<Test> {
        // For JUnit 5
        useJUnitPlatform()

        // This allows to see tests execution progress
        // in the output on the CI.
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
                TestLogEvent.STANDARD_ERROR,
                TestLogEvent.STANDARD_OUT
            )
        }
    } as Closure<Test>)
}
