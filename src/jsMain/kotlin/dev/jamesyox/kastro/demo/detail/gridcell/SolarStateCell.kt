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

package dev.jamesyox.kastro.demo.detail.gridcell

import dev.jamesyox.kastro.demo.GlobalState
import dev.jamesyox.kastro.demo.domCoroutineScope
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.misc.arrow
import dev.jamesyox.kastro.demo.replacingInnerHTML
import dev.jamesyox.kastro.demo.replacingInnerText
import dev.jamesyox.kastro.demo.roundTwoSigFig
import dev.jamesyox.kastro.demo.sol.prettyString
import dev.jamesyox.kastro.demo.svgk.js.svg
import dev.jamesyox.kastro.sol.SolarState
import dev.jamesyox.statik.css
import dev.jamesyox.svg4kt.attr.attrs.ViewBox
import dev.jamesyox.svg4kt.attr.attrs.cx
import dev.jamesyox.svg4kt.attr.attrs.cy
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.r
import dev.jamesyox.svg4kt.attr.attrs.viewBox
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.tags.circle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.Padding
import kotlinx.css.TextAlign
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.minHeight
import kotlinx.css.minWidth
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.width
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import kotlinx.html.js.h3
import kotlinx.html.js.p
import kotlinx.html.js.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr
import org.w3c.dom.HTMLElement

context(_: CoroutineScope)
fun TagConsumer<HTMLElement>.SolarStateCell(globalState: GlobalState) {
    return CurrentStateCell()
        .replacingInnerHTML(
            globalState.solarState.map { SolarStateCell(it, globalState) }
        )
}

private fun SolarStateCell(
    solarState: SolarState,
    globalState: GlobalState,
): List<HTMLElement> = htmlContent {
    val horizonMovementIndicator = solarState.horizonMovementState.arrow
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.spaceEvenly
            alignItems = Align.center
            width = 75.pct
        }
        div {
            css {
                width = 50.px
                height = 50.px
                minWidth = 50.px
                minHeight = 50.px
                borderRadius = 8.px
                padding = Padding(10.px)
                backgroundColor = Color.black
            }
            svg {
                viewBox = ViewBox(minX = -5, minY = -5, width = 10, height = 10)
                circle {
                    r = 5.none
                    cx = 0.none
                    cy = 0.none
                    fill(SvgColor.Yellow)
                }
            }
        }
        div {
            css {
                padding = Padding(8.px)
                textAlign = TextAlign.center
            }
            h3 {
                css { textAlign = TextAlign.center }
                +"Sun"
            }
            p { }.apply {
                context(domCoroutineScope()) {
                    replacingInnerText(globalState.solarCountDownContent)
                }
            }
        }
    }
    table {
        tr {
            th { +"Solar Phase:" }
            td { +solarState.solarPhase.prettyString }
        }
        tr {
            th { +"Visible Altitude:" }
            td { +"${solarState.altitude.roundTwoSigFig()}° $horizonMovementIndicator" }
        }
        tr {
            th { +"Atmospheric Refraction:" }
            td { +"${solarState.atmosphericRefraction.roundTwoSigFig()}°" }
        }
        tr {
            th { +"True Altitude:" }
            td { +"${solarState.trueAltitude.roundTwoSigFig()}° $horizonMovementIndicator" }
        }
        tr {
            th { +"Azimuth:" }
            td { +"${solarState.azimuth.roundTwoSigFig()}°" }
        }
        tr {
            th { +"Distance:" }
            td { +"${solarState.distance.roundTwoSigFig()} km" }
        }
    }
}
