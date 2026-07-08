import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    androidTestImplementation(libs.androidx.compose.uitest.junit4.android)
    androidTestImplementation(libs.androidx.compose.uitest.manifest)
}

// 1. Securely load the keystore properties
private val keystorePropertiesFile = rootProject.file("keystore.properties")
private val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

private enum class InstrumentedTestModes(val mode: String) {
    Connected("connected"), Pixel9aApi36("pixel9aApi36")
}

android {
    namespace = "com.techullurgy.games.sudoku"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.techullurgy.games.sudoku"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            // Only apply this if the properties file actually exists (prevents CI crashes)
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyAliasPassword"] as String
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            // Attach the signing config we just created
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                // "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
        create("uitest") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        managedDevices {
            localDevices {
                create(InstrumentedTestModes.Pixel9aApi36.mode) {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    dependsOn(":shared:testAndroidHostTest")
}

afterEvaluate {
    tasks.matching {
        InstrumentedTestModes.entries.map { it.mode }.any { m -> it.name.startsWith(m) } && it.name.endsWith("Check")
    }.configureEach {
        val instrumentedTestMode = when {
            name.startsWith(InstrumentedTestModes.Connected.mode) -> InstrumentedTestModes.Connected
            name.startsWith(InstrumentedTestModes.Pixel9aApi36.mode) -> InstrumentedTestModes.Pixel9aApi36
            else -> error("No logic found for the Check Task of name $name")
        }

        when(instrumentedTestMode) {
            InstrumentedTestModes.Connected -> {
                dependsOn(":shared:androidConnectedCheck")
            }
            InstrumentedTestModes.Pixel9aApi36 -> {
                dependsOn(":shared:${instrumentedTestMode.mode}Check")
            }
        }
    }

    tasks.matching {
        InstrumentedTestModes.entries.map { it.mode }.any { m -> it.name.startsWith(m) } && it.name.endsWith("AndroidTest")
    }.configureEach {
        val instrumentedTestMode = when {
            name.startsWith(InstrumentedTestModes.Connected.mode) -> InstrumentedTestModes.Connected
            name.startsWith(InstrumentedTestModes.Pixel9aApi36.mode) -> InstrumentedTestModes.Pixel9aApi36
            else -> error("No logic found for the AndroidTest Task of name $name")
        }

        when(instrumentedTestMode) {
            InstrumentedTestModes.Connected -> {
                dependsOn(":shared:connectedAndroidDeviceTest")
            }
            InstrumentedTestModes.Pixel9aApi36 -> {
                dependsOn(":shared:${instrumentedTestMode.mode}AndroidDeviceTest")
            }
        }
    }
}

/*

R8 Configuration Analyzer
=========================
For AGP (9.3.0-alpha05 or later),
    By default, It is enabled
    To disable, android.experimental.r8.enableR8ConfigurationAnalyzer=false (default true) - (Gradle Property)
    Output: build/outputs/mapping/release/configanalyzer.html

For AGP (9.2.x or earlier)
    ./gradlew assembleRelease -Dcom.android.tools.r8.dumpkeepradiushtmltodirectory=<output_directory> (System Property)
    -> output_directory Example = /tmp/r8analysis
* */