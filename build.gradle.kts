/*
 * Copyright 2024 James Yox
 *
 * This file is part of kastro-demo.
 *
 * Kastro-demo is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Kastro-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with kastro-demo. If not, see
 * <https://www.gnu.org/licenses/>.
 */

@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.detekt)
    alias(libs.plugins.benmanes.versions)
}

group = "dev.jamesyox"
version = libs.versions.current.get()

repositories {
    mavenCentral()
    mavenLocal()
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    autoCorrect = true
    config.from(files("$projectDir/detekt/config.yml"))

    dependencies {
        detektPlugins(libs.detekt.formatting)
    }
}

kotlin {
    jvm {
        mainRun {
            mainClass.set("dev.jamesyox.kastro.demo.MainKt")
        }
        binaries {
            executable {
                mainClass.set("dev.jamesyox.kastro.demo.MainKt")
            }
        }
    }
    js {
        useEsModules()
        compilerOptions {
            target.set("es2015")
        }
        binaries.executable()
        browser { }
    }

    sourceSets {
        all {
            languageSettings.enableLanguageFeature("ContextParameters")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        commonMain {
            dependencies {
                implementation(libs.kastro)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.jamesyox.svgMagick)
                implementation(libs.jamesyox.statik)
            }
        }
        jsMain {
            dependencies {
                implementation(npm(name = "leaflet", version = "1.9.4"))
                implementation(libs.kotlin.js.browser)
            }
        }
        jvmMain
    }
}

tasks.register("allDetekt") {
    allprojects {
        this@register.dependsOn(tasks.withType<Detekt>())
    }
}


tasks.getByName<Jar>("jvmJar") {
    dependsOn("copyJsStatikContent")
}

tasks.register<Copy>("copyJsStatikContent") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(webpackTask.outputDirectory.asFile.get())
    into("docs/.")
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

tasks.dependencyUpdates {
    rejectVersionIf {
        candidate.version.isNonStable() && !currentVersion.isNonStable()
    }
}