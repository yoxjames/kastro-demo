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

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SvgCoords private constructor(
    val x: Double,
    val y: Double
) {
    val distance: Double = sqrt(x.pow(2) + y.pow(2))
    val theta: Double = atan2(y, x).degrees
    companion object {
        fun Cartesian(x: Double, y: Double) = SvgCoords(x = x, y = y)
        fun Polar(distance: Double, theta: Double): SvgCoords {
            return Cartesian(
                x = distance * cos(theta.rad),
                y = distance * sin(theta.rad)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as SvgCoords

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "SvgCoords(x=$x, y=$y, distance=$distance, theta=$theta)"
    }
}

val Double.rad get() = this * (PI / 180.0)
val Double.degrees get() = this * (180.0 / PI)
