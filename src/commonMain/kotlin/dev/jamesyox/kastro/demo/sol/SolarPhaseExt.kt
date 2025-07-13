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

import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.kastro.sol.Twilight

val SolarPhase.prettyString get() = when (this) {
    SolarPhase.AstronomicalDawn -> "Astronomical Dawn"
    SolarPhase.AstronomicalDusk -> "Astronomical Dusk"
    SolarPhase.CivilDawn -> "Civil Dawn"
    SolarPhase.CivilDusk -> "Civil Dusk"
    SolarPhase.Day -> "Day"
    SolarPhase.NauticalDawn -> "Nautical Dawn"
    SolarPhase.NauticalDusk -> "Nautical Dusk"
    SolarPhase.Night -> "Night"
}

val SolarPhase.description get() = when (this) {
    SolarPhase.Night -> "strings/solarPhase/night.txt"
    SolarPhase.AstronomicalDawn -> "strings/solarPhase/astronomical_dawn.txt"
    SolarPhase.NauticalDawn -> "strings/solarPhase/nautical_dawn.txt"
    SolarPhase.CivilDawn -> "strings/solarPhase/civil_dawn.txt"
    SolarPhase.Day -> "strings/solarPhase/day.txt"
    SolarPhase.CivilDusk -> "strings/solarPhase/civil_dusk.txt"
    SolarPhase.NauticalDusk -> "strings/solarPhase/nautical_dusk.txt"
    SolarPhase.AstronomicalDusk -> "strings/solarPhase/astronomical_dusk.txt"
}

val SolarPhase.stroke get() = when (this) {
    is Twilight.AstronomicalTwilight -> "#030c63"
    is Twilight.CivilTwilight -> "#00abff"
    is SolarPhase.Day -> "#ffff00"
    is Twilight.NauticalTwilight -> "#003fc5"
    is SolarPhase.Night -> "#350038"
}
