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

package dev.jamesyox.kastro.demo.detail.gridcell

import dev.jamesyox.kastro.sol.LightPhase
import dev.jamesyox.kastro.sol.LightState
import kotlinx.html.TagConsumer
import kotlinx.html.h3
import kotlinx.html.p
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.LightStateCell(
    lightPhase: LightState,
) {
    GridCell {
        h3 { +"Occurs When" }
        p {
            +"The solar angle is between ${lightPhase.angleRange.start} and ${lightPhase.angleRange.endInclusive} "
            +"relative to the horizon during ${lightPhase.phaseType.name.lowercase()}"
        }
    }
}

private val LightPhase.angleRange get() = when (this) {
    is LightPhase.GoldenHour -> LightPhase.GoldenHour.duskAngle..LightPhase.GoldenHour.dawnAngle
    is LightPhase.BlueHour -> LightPhase.BlueHour.dawnAngle..LightPhase.BlueHour.dawnAngle
}

private enum class LightPhaseType {
    Dawn, Dusk
}

private val LightPhase.phaseType: LightPhaseType get() = when (this) {
    LightState.BlueHourDawn -> LightPhaseType.Dawn
    LightState.BlueHourDusk -> LightPhaseType.Dusk
    LightState.GoldenHourDawn -> LightPhaseType.Dawn
    LightState.GoldenHourDusk -> LightPhaseType.Dusk
}
