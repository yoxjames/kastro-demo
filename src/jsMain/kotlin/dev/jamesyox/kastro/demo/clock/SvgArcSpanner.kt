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

package dev.jamesyox.kastro.demo.clock

import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.types.obj.pct
import dev.jamesyox.svgk.tags.circle
import org.w3c.dom.svg.SVGElement
import kotlin.math.PI

fun TagConsumer<SVGElement>.circlePart(
    radius: Double,
    start: Double,
    theta: Double,
    stroke: String,
    strokeWidth: Number,
    strokeOpacity: Double = 100.0,
    onClick: () -> Unit
): SVGElement {
    val totalSize = (2.0 * PI * radius)
    val dashOffset = (start / 360.0) * totalSize
    val spanSize = (theta / 360.0) * totalSize
    return circle(
        r = radius,
        strokeWidth = strokeWidth,
        stroke = stroke,
        fill = "none",
        strokeOpacity = strokeOpacity.pct,
        strokeDashoffset = -dashOffset,
        strokeDasharray = listOf(spanSize, (totalSize - spanSize)),
    ).apply {
        onclick = { onClick() }
    }
}
