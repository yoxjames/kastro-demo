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

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.detekt)
    alias(libs.plugins.benmanes.versions)
    application
}

group = "dev.jamesyox"
version = libs.versions.current.get()

repositories {
    mavenLocal()
    mavenCentral()
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
        withJava()
        jvmToolchain(libs.versions.jvm.get().toInt())
    }
    js {
        binaries.executable()
        browser { }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kastro)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.jamesyox.svgMagick)
                implementation(libs.jamesyox.statik)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm(name = "leaflet", version = "1.9.4"))
                implementation(libs.kotlin.js.browser)
            }
        }
        val jvmMain by getting
    }
}

tasks.register("allDetekt") {
    allprojects {
        this@register.dependsOn(tasks.withType<Detekt>())
    }
}

tasks.named<JavaExec>("run") {
    dependsOn(
        tasks.named<Jar>("jvmJar"),
        tasks.named<Copy>("copyJsStatikContent")
    )
    classpath(tasks.getByName<Jar>("jvmJar"))
}

// include JS artifacts in any generated JAR
tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.outputDirectory.asFile.get(), webpackTask.mainOutputFileName.get())) // bring output file along into the JAR
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
    from(File(webpackTask.outputDirectory.asFile.get(), webpackTask.mainOutputFileName.get()))
    into("docs/.")
}

application {
    mainClass.set("dev.jamesyox.kastro.demo.MainKt")
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