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

import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.kastro.sol.Twilight
import kotlinx.html.TagConsumer
import kotlinx.html.h3
import kotlinx.html.p
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.TwilightCell(
    twilight: Twilight,
) {
    GridCell {
        h3 { +"Occurs When" }
        p {
            +"The solar angle is between ${twilight.angleRange.start} and ${twilight.angleRange.endInclusive} "
            +"relative to the horizon during ${twilight.phaseType.name.lowercase()}"
        }
    }
}

private val Twilight.angleRange get() = when (this) {
    is Twilight.CivilTwilight -> Twilight.CivilTwilight.duskAngle..Twilight.CivilTwilight.dawnAngle
    is Twilight.NauticalTwilight -> Twilight.NauticalTwilight.duskAngle..Twilight.NauticalTwilight.dawnAngle
    is Twilight.AstronomicalTwilight -> Twilight.AstronomicalTwilight.duskAngle..Twilight.AstronomicalTwilight.dawnAngle
}

private enum class TwilightPhaseType {
    Dawn, Dusk
}

private val Twilight.phaseType: TwilightPhaseType get() = when (this) {
    SolarPhase.AstronomicalDawn -> TwilightPhaseType.Dawn
    SolarPhase.AstronomicalDusk -> TwilightPhaseType.Dusk
    SolarPhase.CivilDawn -> TwilightPhaseType.Dawn
    SolarPhase.CivilDusk -> TwilightPhaseType.Dusk
    SolarPhase.NauticalDawn -> TwilightPhaseType.Dawn
    SolarPhase.NauticalDusk -> TwilightPhaseType.Dusk
}
