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
import dev.jamesyox.svg4kt.attr.attrs.ViewBox
import dev.jamesyox.svg4kt.attr.attrs.cx
import dev.jamesyox.svg4kt.attr.attrs.cy
import dev.jamesyox.svg4kt.attr.attrs.d
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.r
import dev.jamesyox.svg4kt.attr.attrs.viewBox
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.circle
import dev.jamesyox.svg4kt.tags.path
import dev.jamesyox.svg4kt.tags.svg
import dev.jamesyox.svg4kt.util.A
import dev.jamesyox.svg4kt.util.M
import dev.jamesyox.svg4kt.util.PathBuilder
import kotlin.math.PI
import kotlin.math.cos

private const val moonRadius = 100.0

context(_: TagConsumer<T>, _: ElementContainer.Svg)
fun <T> Moon(phase: Double): T {
    return svg {
        viewBox = ViewBox(minX = -100, minY = -100, width = 200, height = 200)
        moonOutline {
            path {
                fill(SvgColor.DarkSlateGrey)
                d { shadowPath(phase) }
            }
        }
    }
}

context(_: TagConsumer<*>, _: ElementContainer.Circle, _: ElementContainer.Path)
private fun moonOutline(content: context(TagConsumer<*>, ElementContainer.Path) () -> Unit) {
    circle {
        cx = 0.0.none
        cy = 0.0.none
        fill(SvgColor.White)
        r = moonRadius.none
    }
    content()
}

context(_: PathBuilder)
private fun shadowPath(phase: Double) {
    val isWaning = phase > 180
    val isGibbous = phase in (90.0..270.0)
    val x = moonRadius * cos(phase * (PI / 180.0))
    M(x = 0, y = -moonRadius)
    A(
        rx = moonRadius,
        ry = moonRadius,
        angle = 0,
        largeArcFlag = true,
        sweepFlag = isWaning,
        x = 0,
        y = moonRadius
    )
    A(
        rx = x,
        ry = moonRadius,
        angle = 0,
        largeArcFlag = false,
        sweepFlag = isGibbous xor isWaning,
        x = 0,
        y = -moonRadius
    )
}
