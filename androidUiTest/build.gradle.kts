plugins {
    alias(libs.plugins.androidTest)
}

dependencies {
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
}

android {
    namespace = "com.techullurgy.games.sudoku.uitests"
    compileSdk {
        version =
            release(
                libs.versions.android.compileSdk
                    .get()
                    .toInt()
            )
    }

    defaultConfig {
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // 3. Optional but recommended: Tell it which app variant to test against
        // If you created that "uiTest" variant earlier, put it here.
        // Otherwise, use "release".
        // targetProjectPath will look for this build type in your app module.
        // create("release") {
        // ...
        // }

        create("uitest") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }
    }

    targetProjectPath = ":androidApp"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

androidComponents {
    // The beforeVariants block runs before Gradle even attempts to configure the tasks for the variant
    beforeVariants(selector().withBuildType("debug")) { variantBuilder ->
        // Gradle pretends the debug variant doesn't even exist for this module
        variantBuilder.enable = false
    }
}

afterEvaluate {
    tasks.named("connectedUitestAndroidTest") {
        dependsOn(":androidApp:installUitest")
    }
}
