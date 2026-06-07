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

import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.prettyHour
import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.DominantBaseline
import dev.jamesyox.svg4kt.attr.attrs.FontSize
import dev.jamesyox.svg4kt.attr.attrs.TextAnchor
import dev.jamesyox.svg4kt.attr.attrs.d
import dev.jamesyox.svg4kt.attr.attrs.dominantBaseline
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.fontSize
import dev.jamesyox.svg4kt.attr.attrs.stroke
import dev.jamesyox.svg4kt.attr.attrs.strokeDasharray
import dev.jamesyox.svg4kt.attr.attrs.strokeWidth
import dev.jamesyox.svg4kt.attr.attrs.textAnchor
import dev.jamesyox.svg4kt.attr.attrs.x
import dev.jamesyox.svg4kt.attr.attrs.y
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.categories.container.unaryPlus
import dev.jamesyox.svg4kt.tags.path
import dev.jamesyox.svg4kt.tags.svg
import dev.jamesyox.svg4kt.tags.text
import dev.jamesyox.svg4kt.util.A
import dev.jamesyox.svg4kt.util.M
import org.w3c.dom.svg.SVGElement
import kotlin.math.PI
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

context(
    _: TagConsumer<SVGElement>,
    _: ElementContainer.Svg,
    _: ElementContainer.Path,
    _: ElementContainer.Text
)
fun ClockOverlay(
    radius: Double,
    timeRange: ClosedRange<Instant>
) {
    val hashSize = 0.4
    val totalHours = 24.0
    val startPoint = SvgCoords.Polar(radius, timeRange.computeAngle(timeRange.start))
    val endPoint = SvgCoords.Polar(radius, timeRange.computeAngle(timeRange.endInclusive))
    val totalAngle = startPoint.theta - endPoint.theta
    val circumference = (2.0 * PI * radius) * ((360.0 - totalAngle) / 360.0) - hashSize
    val hourSize = (circumference - (hashSize * totalHours)) / totalHours
    svg {

    }
    path {
        stroke(SvgColor.Gray)
        strokeWidth = 5.0.none
        strokeDasharray = listOf(hashSize, hourSize)
        d {
            M(startPoint.x, startPoint.y)
            A(
                rx = radius,
                ry = radius,
                angle = 0,
                largeArcFlag = true,
                sweepFlag = true,
                x = endPoint.x,
                y = endPoint.y
            )
        }
    }

    for (hour in (0..totalHours.toInt())) {
        val suffix = when {
            hour == 24 -> "am"
            (hour >= 12) -> "pm"
            else -> "am"
        }
        val pos = SvgCoords.Polar(
            distance = radius + 7.0,
            theta = timeRange.computeAngle(timeRange.start + hour.hours)
        )
        text {
            +"${prettyHour(hour)}$suffix"
            fill(SvgColor.Gray)
            fontSize = FontSize.Value(3.0.none)
            x = listOf(pos.x.none)
            y = listOf(pos.y.none)
            dominantBaseline = DominantBaseline.Central
            textAnchor = TextAnchor.Middle
        }
    }
}
