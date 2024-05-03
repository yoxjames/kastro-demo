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

import dev.jamesyox.kastro.demo.clock.Moon
import dev.jamesyox.kastro.demo.svgk.svgMagick
import dev.jamesyox.statik.css.ClassSelector
import dev.jamesyox.statik.css.classSelector
import dev.jamesyox.statik.css.stylesheetFile
import dev.jamesyox.statik.html.indexHtml
import dev.jamesyox.statik.statickSite
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.meta
import kotlinx.html.styleLink
import kotlinx.html.title
import java.io.File

fun main() {
    statickSite(
        currentDirectory = File("."),
        siteDirectory = File("moonTester")
    ) {
        val stylesheet = stylesheetFile(path = "style.css") {
            body {
                backgroundColor = Color.black
            }

            MoonTesterStyle(
                phasesContainer = classSelector("phases-container") {
                    display = Display.flex
                    height = 200.px
                    width = 100.pct
                    flexDirection = FlexDirection.row
                },
                phaseContainer = classSelector("phase-container") {
                }
            )
        }
        indexHtml {
            html {
                lang = "en"
            }
            head {
                meta {
                    charset = "UTF-8"
                }
                styleLink(stylesheet.httpPath)
                title { +"Moon Tester" }
            }

            body {
                div(classes = stylesheet.style.phasesContainer.name) {
                    svgMagick {
                        for (index in (1..16)) {
                            Moon(index * 22.5)
                        }
                    }
                }
            }
        }
    }
}

data class MoonTesterStyle(
    val phasesContainer: ClassSelector,
    val phaseContainer: ClassSelector
)
