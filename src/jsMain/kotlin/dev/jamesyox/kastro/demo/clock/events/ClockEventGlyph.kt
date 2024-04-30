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

package dev.jamesyox.kastro.demo.clock.events

import dev.jamesyox.kastro.demo.clock.SvgCoords
import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.svgk.js.svgMagick
import dev.jamesyox.statik.css
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.objs.transform.Translate
import dev.jamesyox.svgk.tags.g
import kotlinx.css.Padding
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.datetime.Instant
import org.w3c.dom.HTMLElement
import org.w3c.dom.svg.SVGElement

fun TagConsumer<SVGElement>.ClockEventGlyph(
    distance: Double,
    time: Instant,
    timeRange: ClosedRange<Instant>,
    content: TagConsumer<SVGElement>.() -> SVGElement
) {
    val angularPosition = timeRange.computeAngle(time)
    val coords = SvgCoords.Polar(distance, angularPosition)
    g(
        transform = listOf(
            Translate(-1.5, -1.5),
            Translate(coords.x, coords.y),
        )
    ) {
        content().apply {
            setAttribute("width", "4px")
            setAttribute("height", "4px")
        }
    }
}

fun kotlinx.html.TagConsumer<HTMLElement>.EventGlyph(
    content: TagConsumer<SVGElement>.() -> SVGElement
) {
    svgMagick(content).apply {
        setAttribute("height", "25px")
        setAttribute("width", "25px")
        css {
            padding = Padding(4.px)
        }
    }
}
