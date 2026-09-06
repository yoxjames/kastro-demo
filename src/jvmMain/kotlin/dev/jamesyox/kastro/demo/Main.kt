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

package dev.jamesyox.kastro.demo

import dev.jamesyox.kastro.demo.sol.description
import dev.jamesyox.kastro.sol.LightState
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.statik.CurrentDirectory
import dev.jamesyox.statik.FromDirectory
import dev.jamesyox.statik.css
import dev.jamesyox.statik.css.writeCssFile
import dev.jamesyox.statik.fromDirectory
import dev.jamesyox.statik.html.writeHtmlFile
import dev.jamesyox.statik.io.copyFile
import dev.jamesyox.statik.kGenerate
import dev.jamesyox.svg4kt.SvgTagDSL
import dev.jamesyox.svg4kt.attr.AttributeConsumer
import dev.jamesyox.svg4kt.consumers.svgString
import dev.jamesyox.svg4kt.tags.Svg
import kotlinx.css.Padding
import kotlinx.css.TextAlign
import kotlinx.css.fontSize
import kotlinx.css.opacity
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.html.HTMLTag
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.title
import kotlinx.html.unsafe
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

private const val VERSION = "12"

fun main() {
    context(SystemFileSystem) {
        kGenerate(
            currentDirectory = Path("/home/yoxjames/IdeaProjects/kastro-demo/debugSite")
        ) {
            val resourcesDirectory = Path("/home/yoxjames/IdeaProjects/kastro-demo/resources")
            writeCssFile("style.css") {
                KastroDemoStylesheet(KastroDemoStylesheet)
            }
            writeHtmlFile("index.html") {
                html {
                    lang = "en"
                    head {
                        meta {
                            charset = "UTF-8"
                        }
                        meta {
                            name = "viewport"
                            content = "width=device-width,initial-scale=1.0"
                        }
                        meta {
                            name = "keywords"
                            content = "sunrise, sunset, moonrise, moonset"
                        }
                        meta {
                            name = "description"
                            content = "Astronomical calculator for solar and lunar events such as " +
                                    "sunrise, sunset, moonrise, and moonset. "
                        }
                        link {
                            rel = "stylesheet"
                            href = "leaflet.css?v=$VERSION"
                        }
                        styleLink("style.css")
                        title { +"Kastro Demo - Sun and Moon Calculator" }
                    }
                    body {
                        div {
                            id = "kastro-demo-container"
                        }
                        script {
                            src = "kastro-demo.js?v=$VERSION"
                        }
                        footer {
                            p {
                                css {
                                    padding = Padding(16.px)
                                    fontSize = 12.px
                                    opacity = 0.6
                                    textAlign = TextAlign.center
                                }
                                +"Kastro Demo by James Yox."
                                br
                                +"kastro-js:0.6.0"
                            }
                        }
                    }
                }
                fromDirectory(resourcesDirectory) {
                    copyFile("leaflet.css")
                    copyFile("locate.svg")
                    copyFile("back.svg")
                    copyFile("github-mark-white.svg")
                    copyFile("settings.svg")
                    solarPhaseStrings()
                    lightStateStrings()
                }
            }
        }
    }
}

context(
    fileSystem: FileSystem,
    currentDirectory: CurrentDirectory,
    fromDirectory: FromDirectory
)
private fun solarPhaseStrings() {
    copyFile(SolarPhase.Night.description)
    copyFile(SolarPhase.AstronomicalDawn.description)
    copyFile(SolarPhase.NauticalDawn.description)
    copyFile(SolarPhase.CivilDawn.description)
    copyFile(SolarPhase.Day.description)
    copyFile(SolarPhase.CivilDusk.description)
    copyFile(SolarPhase.NauticalDusk.description)
    copyFile(SolarPhase.AstronomicalDusk.description)
}

context(
    fileSystem: FileSystem,
    currentDirectory: CurrentDirectory,
    fromDirectory: FromDirectory
)
private fun lightStateStrings() {
    copyFile(LightState.BlueHourDusk.description)
    copyFile(LightState.BlueHourDawn.description)
    copyFile(LightState.GoldenHourDawn.description)
    copyFile(LightState.GoldenHourDusk.description)
}

fun HTMLTag.svg(
    block: context(AttributeConsumer, @SvgTagDSL Svg) () -> Unit
) {
    unsafe {
        raw(svgString { this@svg.svg { block() } })
    }
}
