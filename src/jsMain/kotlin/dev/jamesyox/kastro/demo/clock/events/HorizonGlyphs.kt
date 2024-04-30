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

import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.objs.ViewBox
import dev.jamesyox.svgk.tags.path
import dev.jamesyox.svgk.tags.svg
import org.w3c.dom.svg.SVGElement

fun TagConsumer<SVGElement>.RiseGlyph(color: String): SVGElement {
    return HorizonGlyph(color = color) {
        path(
            strokeWidth = 0.8,
            stroke = "red",
            fill = "none"
        ) {
            M(0, 3)
            L(0, 1)
            M(-1, 2.0)
            L(0, 1)
            L(1, 2.0)
        }
    }
}

fun TagConsumer<SVGElement>.SetGlyph(color: String): SVGElement {
    return HorizonGlyph(color = color) {
        path(
            strokeWidth = 0.8,
            stroke = "red",
            fill = "none"
        ) {
            M(0, 0.5)
            L(0, 2.5)
            M(-1, 1.5)
            L(0, 2.5)
            L(1, 1.5)
        }
    }
}

internal fun TagConsumer<SVGElement>.HorizonGlyph(
    color: String,
    arrowContent: TagConsumer<SVGElement>.() -> Unit,
): SVGElement {
    return svg(viewBox = ViewBox(minX = -3, minY = -3, width = 6, height = 6)) {
        path(fill = color) {
            M(-3, 0)
            A(
                rx = 3,
                ry = 3,
                angle = 0,
                largeArcFlag = false,
                sweepFlag = true,
                x = 3,
                y = 0
            )
        }
        arrowContent()
    }
}
