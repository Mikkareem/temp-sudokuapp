import dev.detekt.gradle.Detekt

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidKmpLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.kover) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.detekt)
}
private val detektComposeRuleSet = libs.detekt.compose.ruleset
private val detektKtlintWrapper = libs.detekt.ktlint.wrapper

dependencies {
    detektPlugins(detektComposeRuleSet)
    detektPlugins(detektKtlintWrapper)
}

tasks.register<Detekt>("detektAll") {
    description = "Custom DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    source = fileTree(projectDir)
    config = files("$rootDir/config/detekt/detekt.yml")
//    baseline = file("$rootDir/config/detekt/baseline.yaml")
    include("**/*.kt")
    exclude("**/resources/**", "**/build/**")

    reports {
        html { enabled = true }
        checkstyle { enabled = true }
        sarif { enabled = true }
    }
}

//private val detektPlugin = libs.plugins.detekt.get().pluginId
//
//subprojects {
//    plugins.apply(detektPlugin)
//
//    plugins.withId(detektPlugin) {
//        dependencies {
//            "detektPlugins"(detektComposeRuleSet)
//        }
//
//        configure<DetektExtension> {
//        }
//    }
//}


//private val ktlintComposeRuleSet = libs.ktlint.compose.ruleset
//private val kotlinterPlugin = libs.plugins.kotlinter.get().pluginId
//
// Tasks: lintKotlin, formatKotlin
//subprojects {
//    plugins.apply(kotlinterPlugin)
//
//    plugins.withId(kotlinterPlugin) {
//        dependencies {
//            "ktlint"(ktlintComposeRuleSet)
//        }
//
//        configure<KotlinterExtension> {
//            ktlintVersion = "1.8.0"
//            ignoreFormatFailures = false
//            ignoreLintFailures = false
//            reporters = arrayOf("checkstyle", "html", "json")
//        }
//    }
//
//    afterEvaluate {
//        tasks.withType<LintTask> {
//            exclude { it.file.path.contains("generated") }
//            exclude { it.file.path.contains("build") }
//            exclude { it.file.path.contains("ksp") }
//        }
//
//        tasks.withType<FormatTask> {
//            exclude { it.file.path.contains("generated") }
//            exclude { it.file.path.contains("build") }
//            exclude { it.file.path.contains("ksp") }
//        }
//    }
//}


//private val spotlessPlugin = libs.plugins.spotless.get().pluginId
//
// Tasks: spotlessApply, spotlessCheck
//subprojects {
//    plugins.apply(spotlessPlugin)
//
//    plugins.withId(spotlessPlugin) {
//        configure<SpotlessExtension> {
//            kotlin {
//                target("src/**/*.kt")
//                targetExclude("build/**/*.kt")
//                ktlint()
//                    .editorConfigOverride(
//                        mapOf(
//                            "ktlint_standard_no-unused-imports" to "enabled"
//                        )
//                    )
//                    .customRuleSets(
//                        listOf(ktlintComposeRuleSet.get().toString())
//                    )
//            }
//
//            kotlinGradle {
//                target("*.kts")
//                target("**/*.kts")
//                targetExclude("build/**/*.kts")
//                ktlint()
//                    .editorConfigOverride(
//                        mapOf(
//                            "android" to "true",
//                            "ktlint_standard_no-unused-imports" to "enabled"
//                        )
//                    )
//            }
//        }
//    }
//}

//private val ktlintPluginId =
//    libs.plugins.ktlint.gradle
//        .get()
//        .pluginId
//
//subprojects {
//    plugins.apply(ktlintPluginId)
//
//    plugins.withId(ktlintPluginId) {
//        dependencies {
//            "ktlintRuleset"(ktlintComposeRuleSet)
//        }
//
//        configure<KtlintExtension> {
//            verbose = true
//            android = true
//            ignoreFailures = false
//            enableExperimentalRules = true
//
//            reporters {
//                reporter(ReporterType.CHECKSTYLE)
//                reporter(ReporterType.JSON)
//                reporter(ReporterType.HTML)
//            }
//
//            filter {
//                exclude { it.file.path.contains("build/generated") }
//                exclude { it.file.path.contains("ksp") }
//                exclude { it.file.path.contains("Confetti.kt") }
//                exclude { it.file.path.contains("ConfettiTest.kt") }
//                exclude { it.file.path.contains("BackgroundTiles.kt") }
//            }
//        }
//    }
//}