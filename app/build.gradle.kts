/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Version compativility for Kotlin Gradle Plugin vs Gradle vs AGP:
// https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.navigation.safe.args)
    jacoco
    // jacoco github actions example:
    // https://github.com/marketplace/actions/jacoco-report
    // jacoco command line invocation:
    // ./gradlew :app:createDebugCoverageReport
    alias(libs.plugins.ksp) // might brake the below
    // kapt needs to be the last statement https://github.com/google/dagger/issues/2040
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "de.blumesladen"
    compileSdk = 34

    hilt {
        enableAggregatingTask = true
    }
    testCoverage {
        // note there is a bug causing this version to be right now ignored. To be fixed by Google
        // https://issuetracker.google.com/issues/324271174
        version = "0.8.11"
    }
    defaultConfig {
        applicationId = "de.blumesladen"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "de.blumesladen.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Enable room auto-migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to version 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.register<JacocoReport>("unitTestCoverageReport") {
    // dependsOn: "testLocalDebugUnitTest"
    group = "Verification" // existing group containing tasks for generating linting reports etc.
    description = "Generate Jacoco coverage reports for the 'local' debug build."


    reports {
        // human readable (written into './build/reports/jacoco/unitTestCoverageReport/html')
        html.required.set(true)
        // CI-readable (written into './build/reports/jacoco/unitTestCoverageReport/unitTestCoverageReport.xml')
        xml.required.set(true) // required for CI
    }
    // Execution data generated when running the tests against classes instrumented by the JaCoCo agent.
    // This is enabled with 'enableUnitTestCoverage' in the 'debug' build type.
    executionData.setFrom(
        fileTree("${project.layout.buildDirectory}/test-results/instrumentation_results"))

    // Compiled Kotlin class files are written into build-variant-specific subdirectories of 'build/tmp/kotlin-classes'.
    classDirectories.setFrom(
        fileTree(project.layout.buildDirectory) {
            include("**/tmp/kotlin-classes/debug/**")
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/*Application.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "**/android/**/*.*",
                "**/androidx/**/*.*",
                "**/di/**/*.*",
                "**/*Dagger*.*",
                "**/*Screen*"
            )
        }
    )

    // To produce an accurate report, the bytecode is mapped back to the original source code.
    sourceDirectories.setFrom(
        fileTree("{$project.projectDir}/src/main/java/**"),
        fileTree("{$project.projectDir}/src/main/kotlin/**")
    )
}

dependencies {
    implementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.material)
    implementation(libs.composecalendar)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.mockito.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    androidTestImplementation(libs.turbine)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented tests: jUnit rules and runners

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.navigation.testing)
}

kapt {
    correctErrorTypes = true
}