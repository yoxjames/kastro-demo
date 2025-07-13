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

package dev.jamesyox.kastro.demo.sol

import dev.jamesyox.kastro.sol.LightState

private const val blueHourColor = "#4242f5"
private const val goldenHourColor = "#DAA520"

val LightState.stroke get() = when (this) {
    LightState.BlueHourDawn, LightState.BlueHourDusk -> blueHourColor
    LightState.GoldenHourDawn, LightState.GoldenHourDusk -> goldenHourColor
}

val LightState.radiusOffset get() = when (this) {
    LightState.BlueHourDawn, LightState.BlueHourDusk -> -4
    LightState.GoldenHourDawn, LightState.GoldenHourDusk -> 0
}

val LightState.prettyString get() = when (this) {
    LightState.BlueHourDawn -> "Blue Hour (Dawn)"
    LightState.BlueHourDusk -> "Blue Hour (Dusk)"
    LightState.GoldenHourDawn -> "Golden Hour (Dawn)"
    LightState.GoldenHourDusk -> "Golden Hour (Dusk)"
}

val LightState.description get() = when (this) {
    LightState.BlueHourDawn -> "strings/lightState/blue_hour_dawn.txt"
    LightState.BlueHourDusk -> "strings/lightState/blue_hour_dusk.txt"
    LightState.GoldenHourDawn -> "strings/lightState/golden_hour_dawn.txt"
    LightState.GoldenHourDusk -> "strings/lightState/golden_hour_dusk.txt"
}
