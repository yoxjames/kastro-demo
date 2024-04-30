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
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.statik.text.promise

object SolarPhaseDescriptions {
    val night = SolarPhase.Night.description.promise()
    val astronomicalDawn = SolarPhase.AstronomicalDawn.description.promise()
    val nauticalDawn = SolarPhase.NauticalDawn.description.promise()
    val civilDawn = SolarPhase.CivilDawn.description.promise()
    val day = SolarPhase.Day.description.promise()
    val civilDusk = SolarPhase.CivilDusk.description.promise()
    val nauticalDusk = SolarPhase.NauticalDusk.description.promise()
    val astronomicalDusk = SolarPhase.AstronomicalDusk.description.promise()
}

object SolarLightDescriptions {
    val blueHourDawn = LightState.BlueHourDawn.description.promise()
    val blueHourDusk = LightState.BlueHourDusk.description.promise()
    val goldenHourDawn = LightState.GoldenHourDawn.description.promise()
    val goldenHourDusk = LightState.GoldenHourDusk.description.promise()
}
