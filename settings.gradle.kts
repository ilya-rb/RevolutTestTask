plugins {
    id("com.gradle.enterprise") version "3.1.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

include(
    ":app",
    ":libs:ui",
    ":libs:tools",
    ":libs:util"
)

rootProject.name = "Revolut Test"