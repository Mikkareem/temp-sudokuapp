import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotest)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.kover)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.set(
            listOf(
                // Stable in KT-2.4.x, Added because of Compatibility issues with Android Studio (Quail).
                // Will be fixed in Future releases of Android Studio
                // TODO: Delete the CompilerArg (Explicit-Backing-Field)
                "-Xexplicit-backing-fields",
                // Stable in KT-2.4.x, Added because of Compatibility issues with Android Studio (Quail).
                // Will be fixed in Future releases of Android Studio
                // TODO: Delete the CompilerArg (Context-Parameters)
                "-Xcontext-parameters",
            )
        )
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "com.techullurgy.sudoku.shared"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        androidResources {
            enable = true
        }
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            managedDevices {
                localDevices {
                    create("pixel9aApi36") {
                        device = "Pixel 2"
                        apiLevel = 30
                        systemImageSource = "aosp-atd"
                    }
                }
            }
        }
        withHostTest {
            isIncludeAndroidResources = true
        }

        packaging {
            // Ignore the conflicting license files during packaging
            resources.excludes.add("META-INF/AL2.0")
            resources.excludes.add("META-INF/LGPL2.1") // Good practice to add this one too
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)
            implementation(libs.koin.annotations)

            implementation(libs.jetbrains.androidx.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.navigation3.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.test)

            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.assertions.core)

            implementation(projects.commonTestUtils)
        }

        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.test.junit4)
        }

        try {
            getByName("androidHostTest").dependencies {
                implementation(libs.kotest.runner.junit5) // junit4

                implementation(libs.robolectric)
                implementation(libs.androidx.compose.uitest.junit4.android)
                implementation(libs.androidx.compose.uitest.manifest)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test.junit4)

                implementation(projects.commonTestUtils)
            }
        } catch (_: UnknownDomainObjectException) {
        }

        try {
            getByName("androidDeviceTest").dependencies {
                implementation(libs.androidx.compose.uitest.junit4.android)
                implementation(libs.androidx.compose.uitest.manifest)

                implementation(libs.kotest.runner.junit4)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test.junit4)
            }
        } catch (_: UnknownDomainObjectException) {
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

kover {

}

tasks.withType<Test>().configureEach {
//    if (name in setOf("testAndroidHostTest", "jvmTest")) {
    if (name in setOf("jvmTest")) {
        useJUnitPlatform()
        filter {
            isFailOnNoMatchingTests = false
        }
    }
}

tasks.withType<AbstractTestTask>().configureEach {
    outputs.upToDateWhen { false }
}