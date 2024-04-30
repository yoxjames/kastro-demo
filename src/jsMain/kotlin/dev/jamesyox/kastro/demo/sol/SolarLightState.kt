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

import dev.jamesyox.kastro.demo.distinctUntilChangedBy
import dev.jamesyox.kastro.sol.LightPhase
import dev.jamesyox.kastro.sol.LightState
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.kastro.sol.calculateSolarState
import kotlinx.datetime.Instant

data class SolarLightState(
    val start: Instant,
    val end: Instant,
    val lightState: LightState,
    val startEvent: SolarEvent?,
    val endEvent: SolarEvent?
)

private data class SolarLightStateTime(
    val start: Instant,
    val startEvent: SolarEvent?,
    val lightStates: List<LightState>,
)

fun List<SolarEvent>.solarLightState(
    timeRange: ClosedRange<Instant>,
    location: Pair<Double, Double>
): List<SolarLightState> {
    val states = scan(
        SolarLightStateTime(
            start = timeRange.start,
            startEvent = null,
            lightStates = timeRange.start.calculateSolarState(location).lightStates
        )
    ) { acc, solarEvent ->
        SolarLightStateTime(
            start = solarEvent.time,
            startEvent = solarEvent,
            lightStates = calculateLightState(acc.lightStates, solarEvent)
        )
    }

    val goldenHourStates: List<SolarLightState> = states.map {
        SolarLightStateTime(
            start = it.start,
            startEvent = it.startEvent,
            lightStates = it.lightStates.filterIsInstance<LightPhase.GoldenHour>() as List<LightState>
        )
    }.distinctUntilChangedBy { it.lightStates }
        .zipWithNext()
        .mapNotNull {
            it.first.lightStates.firstOrNull()?.let { goldenHour ->
                SolarLightState(
                    start = it.first.start,
                    end = it.second.start,
                    lightState = goldenHour,
                    startEvent = it.first.startEvent,
                    endEvent = it.second.startEvent
                )
            }
        }

    val blueHourStates: List<SolarLightState> = states.map {
        SolarLightStateTime(
            start = it.start,
            startEvent = it.startEvent,
            lightStates = it.lightStates.filterIsInstance<LightPhase.BlueHour>() as List<LightState>
        )
    }.distinctUntilChangedBy { it.lightStates }
        .zipWithNext()
        .mapNotNull {
            it.first.lightStates.firstOrNull()?.let { blueHour ->
                SolarLightState(
                    start = it.first.start,
                    end = it.second.start,
                    lightState = blueHour,
                    startEvent = it.first.startEvent,
                    endEvent = it.second.startEvent
                )
            }
        }

    return goldenHourStates + blueHourStates
}

fun calculateLightState(currentState: List<LightState>, event: SolarEvent): List<LightState> = when (event) {
    is SolarEvent.Sunrise, is SolarEvent.Sunset -> currentState
    is SolarEvent.SunriseEnd, is SolarEvent.SunsetBegin -> currentState
    is SolarEvent.BlueHourDawn -> currentState + LightState.BlueHourDawn
    is SolarEvent.BlueHourDawnEnd -> currentState - LightState.BlueHourDawn
    is SolarEvent.BlueHourDusk -> currentState + LightState.BlueHourDusk
    is SolarEvent.BlueHourDuskEnd -> currentState - LightState.BlueHourDusk
    is SolarEvent.GoldenHourDawn -> currentState + LightState.GoldenHourDawn
    is SolarEvent.GoldenHourDawnEnd -> currentState - LightState.GoldenHourDawn
    is SolarEvent.GoldenHourDusk -> currentState + LightState.GoldenHourDusk
    is SolarEvent.GoldenHourDuskEnd -> currentState - LightState.GoldenHourDusk
    is SolarEvent.Noon -> currentState.map {
        when (it) {
            is LightState.GoldenHourDawn -> LightState.GoldenHourDusk
            is LightState.BlueHourDawn -> LightState.BlueHourDusk
            else -> it
        }
    }

    is SolarEvent.Nadir -> currentState.map {
        when (it) {
            is LightState.GoldenHourDusk -> LightState.GoldenHourDawn
            is LightState.BlueHourDusk -> LightState.BlueHourDawn
            else -> it
        }
    }
    is SolarEvent.AstronomicalDawn, is SolarEvent.AstronomicalDusk -> currentState
    is SolarEvent.CivilDawn, is SolarEvent.CivilDusk -> currentState
    is SolarEvent.Day, is SolarEvent.Night -> currentState
    is SolarEvent.NauticalDawn, is SolarEvent.NauticalDusk -> currentState
}
