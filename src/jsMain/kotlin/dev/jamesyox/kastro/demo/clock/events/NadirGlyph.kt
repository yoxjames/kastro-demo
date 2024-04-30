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

import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.objs.ViewBox
import dev.jamesyox.svgk.tags.path
import dev.jamesyox.svgk.tags.svg
import org.w3c.dom.svg.SVGElement

fun TagConsumer<SVGElement>.NadirGlyph(): SVGElement {
    return svg(viewBox = ViewBox(minX = -3, minY = -3, width = 6, height = 6)) {
        path(
            strokeWidth = 0.2,
            stroke = "white",
            fill = SolarPhase.Night.stroke
        ) {
            M(-3, -3)
            L(0, 3)
            L(3, -3)
            Z
        }
    }
}
