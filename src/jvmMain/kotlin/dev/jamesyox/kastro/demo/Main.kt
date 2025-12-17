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
import dev.jamesyox.statik.StaticSiteContext
import dev.jamesyox.statik.css
import dev.jamesyox.statik.css.writeCssFile
import dev.jamesyox.statik.html.writeHtmlFile
import dev.jamesyox.statik.io.copyResource
import dev.jamesyox.statik.staticSite
import kotlinx.css.Padding
import kotlinx.css.TextAlign
import kotlinx.css.fontSize
import kotlinx.css.opacity
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.textAlign
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
import kotlinx.io.files.Path

private const val VERSION = "10"

fun main() {
    staticSite(
        root = Path("/home/yoxjames/IdeaProjects/kastro-demo/docs")
    ) {
        val resourcesDirectory = Path("/home/yoxjames/IdeaProjects/kastro-demo/resources")
        writeCssFile(Path("style.css")) {
            KastroDemoStylesheet(KastroDemoStylesheetAgreement)
        }
        writeHtmlFile(Path("index.html")) {
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
                            +"kastro-js:0.5.0"
                        }
                    }
                }
            }
            copyResource(resourcesDirectory, Path("leaflet.css"))
            copyResource(resourcesDirectory, Path("locate.svg"))
            copyResource(resourcesDirectory, Path("back.svg"))
            copyResource(resourcesDirectory, Path("github-mark-white.svg"))
            copyResource(resourcesDirectory, Path("settings.svg"))
            solarPhaseStrings(resourcesDirectory)
            lightStateStrings(resourcesDirectory)
        }
    }
}

private fun StaticSiteContext.solarPhaseStrings(resourcesDirectory: Path) {
    copyResource(resourcesDirectory, Path(SolarPhase.Night.description))
    copyResource(resourcesDirectory, Path(SolarPhase.AstronomicalDawn.description))
    copyResource(resourcesDirectory, Path(SolarPhase.NauticalDawn.description))
    copyResource(resourcesDirectory, Path(SolarPhase.CivilDawn.description))
    copyResource(resourcesDirectory, Path(SolarPhase.Day.description))
    copyResource(resourcesDirectory, Path(SolarPhase.CivilDusk.description))
    copyResource(resourcesDirectory, Path(SolarPhase.NauticalDusk.description))
    copyResource(resourcesDirectory, Path(SolarPhase.AstronomicalDusk.description))
}

private fun StaticSiteContext.lightStateStrings(resourcesDirectory: Path) {
    copyResource(resourcesDirectory, Path(LightState.BlueHourDusk.description))
    copyResource(resourcesDirectory, Path(LightState.BlueHourDawn.description))
    copyResource(resourcesDirectory, Path(LightState.GoldenHourDawn.description))
    copyResource(resourcesDirectory, Path(LightState.GoldenHourDusk.description))
}
