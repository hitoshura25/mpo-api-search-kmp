@file:OptIn(ExperimentalDistributionDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotest.multiplatform)
}

group = "com.vmenon.mpo.user.api"
version = "0.0.1"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    js(IR) {
        moduleName = "@hitoshura25/mpo-api-search-kmp"
        browser {
            useCommonJs()
            commonWebpackConfig {
                outputFileName = "mpo-api-search-kmp.js"
            }
        }
        binaries.executable()
        generateTypeScriptDefinitions()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "@hitoshura25/mpo-api-search-kmp"
        browser {
            useCommonJs()
            commonWebpackConfig {
                outputFileName = "mpo-api-search-kmp.js"
            }
        }
        binaries.executable()
        generateTypeScriptDefinitions()
    }
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.property)
                implementation(libs.kotest.framework.engine)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}

android {
    namespace = "com.vmenon.mpo.user.api"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    //signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "MPO Search API KMP"
        description = "A library intended to share search business logic"
        inceptionYear = "2024"
        url = "https://github.com/hitoshura25/mpo-api-search-kmp/"
        licenses {
            license {
                name = "XXX"
                url = "YYY"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "XXX"
                name = "YYY"
                url = "ZZZ"
            }
        }
        scm {
            url = "XXX"
            connection = "YYY"
            developerConnection = "ZZZ"
        }
    }
}