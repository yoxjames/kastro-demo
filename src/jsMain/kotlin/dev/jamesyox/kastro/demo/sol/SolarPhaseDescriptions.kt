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
import js.promise.Promise
import web.http.fetchAsync

object SolarPhaseDescriptions {
    val night = SolarPhase.Night.description.fetchText()
    val astronomicalDawn = SolarPhase.AstronomicalDawn.description.fetchText()
    val nauticalDawn = SolarPhase.NauticalDawn.description.fetchText()
    val civilDawn = SolarPhase.CivilDawn.description.fetchText()
    val day = SolarPhase.Day.description.fetchText()
    val civilDusk = SolarPhase.CivilDusk.description.fetchText()
    val nauticalDusk = SolarPhase.NauticalDusk.description.fetchText()
    val astronomicalDusk = SolarPhase.AstronomicalDusk.description.fetchText()
}

object SolarLightDescriptions {
    val blueHourDawn = LightState.BlueHourDawn.description.fetchText()
    val blueHourDusk = LightState.BlueHourDusk.description.fetchText()
    val goldenHourDawn = LightState.GoldenHourDawn.description.fetchText()
    val goldenHourDusk = LightState.GoldenHourDusk.description.fetchText()
}

private fun String.fetchText(): Promise<String> =  fetchAsync(this).flatThen { it.textAsync() }