@file:OptIn(ExperimentalDistributionDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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
version = property("version")!!

val myModuleName = "@hitoshura25/mpo-api-search-kmp"

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
        moduleName = myModuleName
        useEsModules()
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
        generateTypeScriptDefinitions()
        val typesFile = "kotlin/${myModuleName}.d.ts"
        compilations["main"].packageJson {
            types = typesFile
        }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = myModuleName
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
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

tasks {
    // This is a hack unfortunately as the types file entry above does not seem to always stick when getting generated
    val checkPackageJson = register("checkPackageJson") {
        inputs.property("jsonFile", rootProject.file("build/js/packages/$myModuleName/package.json"))
        inputs.property("moduleName", myModuleName)
        doLast {
            val packageJsonFile = inputs.properties["jsonFile"] as File
            val moduleName = inputs.properties["moduleName"] as String
            println("Checking package.json file: $packageJsonFile")
            if (packageJsonFile.exists()) {
                @Suppress("UNCHECKED_CAST")
                val packageJson = JsonSlurper().parse(packageJsonFile) as MutableMap<String, Any>
                if (!packageJson.contains("types")) {
                    packageJson["types"] = "kotlin/$moduleName.d.ts"
                    val formattedJson = JsonOutput.prettyPrint(JsonOutput.toJson(packageJson))
                    packageJsonFile.writeText(formattedJson)
                    println("package.json updated with the correct types entry.")
                } else {
                    println("package.json contains the correct types entry.")
                }
            } else {
                throw GradleException("package.json file $packageJsonFile not found.")
            }
        }
    }
    named("jsBrowserProductionWebpack").configure { finalizedBy(checkPackageJson) }
}