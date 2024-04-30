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
import dev.jamesyox.kastro.demo.clock.Moon
import dev.jamesyox.kastro.demo.domCoroutineScope
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.luna.illuminationIndicator
import dev.jamesyox.kastro.demo.luna.prettyString
import dev.jamesyox.kastro.demo.misc.arrow
import dev.jamesyox.kastro.demo.replacingInnerHTML
import dev.jamesyox.kastro.demo.replacingInnerText
import dev.jamesyox.kastro.demo.roundTwoSigFig
import dev.jamesyox.kastro.demo.svgk.js.svgMagick
import dev.jamesyox.kastro.luna.LunarState
import dev.jamesyox.kastro.luna.closestPhase
import dev.jamesyox.statik.css
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
import kotlinx.html.h3
import kotlinx.html.js.div
import kotlinx.html.js.p
import kotlinx.html.js.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.LunarStateCell(
    coroutineScope: CoroutineScope,
    globalState: GlobalState,
) {
    return CurrentStateCell()
        .replacingInnerHTML(
            coroutineScope,
            globalState.lunarState.map {
                LunarStateCell(it, globalState)
            }
        )
}

private fun LunarStateCell(
    lunarState: LunarState,
    globalState: GlobalState
): List<HTMLElement> = htmlContent {
    val illuminationMovementIndicator = lunarState.illuminationIndicator
    val horizonIndicator = lunarState.horizonMovementState.arrow
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.spaceEvenly
            alignItems = Align.start
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
            svgMagick { Moon(lunarState.illumination.phase) }
        }
        div {
            css {
                padding = Padding(8.px)
                textAlign = TextAlign.center
            }
            h3 {
                css { textAlign = TextAlign.center }
                +"Moon"
            }
            p { }.apply {
                val coroutineScope = domCoroutineScope()
                replacingInnerText(coroutineScope, globalState.lunarCountDownContent)
            }
        }
    }
    table {
        tr {
            th { +"Actual Phase:" }
            td { +lunarState.phase.prettyString }
        }
        tr {
            th { +"Closest Phase:" }
            td { +lunarState.illumination.closestPhase.prettyString }
        }
        tr {
            th { +"Illumination:" }
            td { +"${(lunarState.illumination.fraction * 100).roundTwoSigFig()}% $illuminationMovementIndicator" }
        }
        tr {
            th { +"Altitude:" }
            td { +"${lunarState.position.altitude.roundTwoSigFig()}° $horizonIndicator" }
        }
        tr {
            th { +"Azimuth:" }
            td { +"${lunarState.position.azimuth.roundTwoSigFig()}°" }
        }
        tr {
            th { +"Distance:" }
            td { +"${lunarState.position.distance.roundTwoSigFig()} km" }
        }
    }
}
