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

import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.r
import dev.jamesyox.svg4kt.attr.attrs.stroke
import dev.jamesyox.svg4kt.attr.attrs.strokeDasharray
import dev.jamesyox.svg4kt.attr.attrs.strokeDashoffset
import dev.jamesyox.svg4kt.attr.attrs.strokeOpacity
import dev.jamesyox.svg4kt.attr.attrs.strokeWidth
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.SvgPaint
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.circle
import org.w3c.dom.svg.SVGElement
import kotlin.math.PI

context(_: TagConsumer<SVGElement>, _: ElementContainer.Circle)
fun circlePart(
    radius: Double,
    start: Double,
    theta: Double,
    outlineColor: SvgColor,
    outlineWidth: Number,
    outlineOpacity: Double = 100.0,
    onClick: () -> Unit
): SVGElement {
    val totalSize = (2.0 * PI * radius)
    val dashOffset = (start / 360.0) * totalSize
    val spanSize = (theta / 360.0) * totalSize
    return circle {
        r = radius.none
        strokeWidth = outlineWidth.none
        stroke(outlineColor)
        fill = SvgPaint.None
        strokeOpacity = (outlineOpacity / 100.0).toFloat()
        strokeDashoffset = (-dashOffset).none
        strokeDasharray = listOf(spanSize, (totalSize - spanSize))
    }.apply {
        onclick = { onClick() }
    }
}
