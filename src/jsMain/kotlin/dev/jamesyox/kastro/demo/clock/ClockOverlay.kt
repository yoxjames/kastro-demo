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
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.enums.DominantBaseline
import dev.jamesyox.svgk.attr.enums.TextAnchor
import dev.jamesyox.svgk.tags.path
import dev.jamesyox.svgk.tags.text
import kotlinx.datetime.Instant
import org.w3c.dom.svg.SVGElement
import kotlin.math.PI
import kotlin.time.Duration.Companion.hours

fun TagConsumer<SVGElement>.ClockOverlay(
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
    path(
        stroke = "gray",
        strokeWidth = 5.0,
        strokeDasharray = listOf(hashSize, hourSize),
    ) {
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
        text(
            text = "${prettyHour(hour)}$suffix",
            fill = "gray",
            fontSize = 3.0,
            x = pos.x,
            y = pos.y,
            dominantBaseline = DominantBaseline.Central,
            textAnchor = TextAnchor.Middle
        )
    }
}
