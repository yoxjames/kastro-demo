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
import dev.jamesyox.statik.StatickSiteContext
import dev.jamesyox.statik.copyStatickFile
import dev.jamesyox.statik.css
import dev.jamesyox.statik.css.link
import dev.jamesyox.statik.css.stylesheetFile
import dev.jamesyox.statik.html.indexHtml
import dev.jamesyox.statik.statickSite
import dev.jamesyox.statik.text.copyTextFile
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
import kotlinx.html.title
import java.io.File

private const val VERSION = "5"

fun main() {
    statickSite(
        currentDirectory = File("resources"),
        siteDirectory = File("docs")
    ) {
        stylesheetFile(stylesheetReference = KastroDemoCssAgreement) {
            KastroDemoStylesheet(KastroDemoStylesheetAgreement)
        }
        indexHtml {
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
                    link(KastroDemoCssAgreement)
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
                            +"kastro-js:0.2.0"
                        }
                    }
                }
            }
            copyStatickFile("leaflet.css")
            copyStatickFile("locate.svg")
            copyStatickFile("back.svg")
            copyStatickFile("github-mark-white.svg")
            copyStatickFile("settings.svg")
            solarPhaseStrings()
            lightStateStrings()
        }
    }
}

private fun StatickSiteContext.solarPhaseStrings() {
    copyTextFile(SolarPhase.Night.description)
    copyTextFile(SolarPhase.AstronomicalDawn.description)
    copyTextFile(SolarPhase.NauticalDawn.description)
    copyTextFile(SolarPhase.CivilDawn.description)
    copyTextFile(SolarPhase.Day.description)
    copyTextFile(SolarPhase.CivilDusk.description)
    copyTextFile(SolarPhase.NauticalDusk.description)
    copyTextFile(SolarPhase.AstronomicalDusk.description)
}

private fun StatickSiteContext.lightStateStrings() {
    copyTextFile(LightState.BlueHourDusk.description)
    copyTextFile(LightState.BlueHourDawn.description)
    copyTextFile(LightState.GoldenHourDawn.description)
    copyTextFile(LightState.GoldenHourDusk.description)
}
