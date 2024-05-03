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

package dev.jamesyox.kastro.demo.view

import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.statik.css
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.ListStyleType
import kotlinx.css.Padding
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.listStyleType
import kotlinx.css.padding
import kotlinx.css.paddingRight
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.br
import kotlinx.html.cite
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.js.h3
import kotlinx.html.js.ol
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.ul
import org.w3c.dom.HTMLElement

private const val aboutContent1 = """
    At the most basic level this is a site that can tell you information about the sun and moon. 
    This site was created primarily to showcase a library I wrote called Kastro implements some basic 
    astronomical calculations pertaining to the sun and moon. Both Kastro and the Kastro Demo were written in Kotlin
    and transpiled to Javascript using Kotlin Multiplatform.
    """

fun AboutView() = htmlContent {
    div {
        css {
            padding = Padding(vertical = 12.px)
        }
        h3 {
            +"What is this?"
        }
        p {
            +aboutContent1
        }
        br
        p {
            +"I had three main goals in making this site:"
        }
        ol {
            li { +"To serve as interactive documentation of what Kastro can calculate." }
            li { +"To make it easy to identify and reproduce potential bugs in the library." }
            li { +"To make a cool site that is potentially useful." }
        }
        h3 {
            +"Source Code"
        }
        ul {
            li {
                css {
                    listStyleType = ListStyleType.none
                }
                a {
                    css {
                        display = Display.inlineFlex
                        alignItems = Align.center
                        flexDirection = FlexDirection.row
                    }
                    GitHubLogo()
                    href = "https://github.com/yoxjames/Kastro"
                    +"Kastro"
                }
            }
            li {
                css {
                    listStyleType = ListStyleType.none
                }
                a {
                    css {
                        display = Display.inlineFlex
                        alignItems = Align.center
                        flexDirection = FlexDirection.row
                    }
                    GitHubLogo()
                    href = "https://github.com/yoxjames/kastro-demo"
                    +"Kastro Demo (this page)"
                }
            }
        }
        h3 {
            +"References"
        }
        ul {
            webCitation(
                title = "The Blue Hour",
                url = "http://www.timeanddate.com/astronomy/blue-hour.html",
                retrieved = "April 11, 2024",
                name = "timeanddate.com"
            )
            webCitation(
                title = "Golden Hour – When Sunlight turns Magical",
                url = "https://www.timeanddate.com/astronomy/golden-hour.html",
                retrieved = "April 11, 2024",
                name = "timeanddate.com"
            )
            li {
                span {
                    +"Körber, Richard. commons-suncalc, (2024) "
                    a {
                        href = "https://github.com/shred/commons-suncalc"
                        +"https://github.com/shred/commons-suncalc"
                    }
                }
            }
            li {
                span {
                    +"Meeus, Jean. Astronomical Algorithms. Richmond, Va., Willmann-Bell, 1998"
                }
            }
            li {
                span {
                    +"Montenbruck, Oliver, and Thomas Pfleger. Astronomy on the Personal Computer. Springer, 14 Mar. "
                    +"2013."
                }
            }
            webCitation(
                title = "Moon Phases | Phases, Eclipses & Supermoons.",
                url = "https://moon.nasa.gov/moon-in-motion/phases-eclipses-supermoons/moon-phases/",
                retrieved = "April 11, 2024",
                name = "Moon: NASA Science"
            )
            webCitation(
                title = "Definitions of Twilight",
                url = "www.weather.gov/fsd/twilight",
                retrieved = "April 11, 2024",
                name = "US Department of Commerce, NOAA"
            )
            webCitation(
                title = "Rise, Set, and Twilight Definition",
                url = "https://aa.usno.navy.mil/faq/RST_defs",
                retrieved = "April 11, 2024",
                name = "US Navy"
            )
        }
    }
}

private fun TagConsumer<*>.webCitation(
    title: String,
    url: String,
    retrieved: String,
    name: String,
) {
    li {
        span {
            a {
                href = url
                cite { +title }
            }
            +" $name, Retrieved $retrieved"
        }
    }
}

private fun TagConsumer<HTMLElement>.GitHubLogo() {
    img {
        css {
            paddingRight = 8.px
            height = 16.px
            width = 16.px
        }
        src = "github-mark-white.svg"
    }
}
