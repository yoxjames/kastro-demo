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
import dev.jamesyox.kastro.demo.svgk.js.svg
import dev.jamesyox.statik.css
import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.AttributeConsumer
import dev.jamesyox.svg4kt.attr.attrs.transform
import dev.jamesyox.svg4kt.tags.Svg
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.g
import dev.jamesyox.svg4kt.util.translate
import kotlinx.css.Padding
import kotlinx.css.padding
import org.w3c.dom.HTMLElement
import org.w3c.dom.svg.SVGElement
import kotlin.time.Instant
import kotlinx.css.px as csspx

context(_: TagConsumer<SVGElement>, _: ElementContainer.G)
fun ClockEventGlyph(
    distance: Double,
    time: Instant,
    timeRange: ClosedRange<Instant>,
    content: context(TagConsumer<SVGElement>, ElementContainer.G) () -> SVGElement
) {
    val angularPosition = timeRange.computeAngle(time)
    val coords = SvgCoords.Polar(distance, angularPosition)
    g {
        transform {
            translate(-1.5, -1.5)
            translate(coords.x, coords.y)
        }
        val result = content()
        result.setAttribute("width", "4px")
        result.setAttribute("height", "4px")
    }
}

fun kotlinx.html.TagConsumer<HTMLElement>.EventGlyph(
    content: context(TagConsumer<SVGElement>, Svg, AttributeConsumer) () -> Unit
) {
    svg { content() }.apply {
        setAttribute("height", "25px")
        setAttribute("width", "25px")
        css {
            padding = Padding(4.csspx)
        }
    }
}
