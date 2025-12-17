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
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.kastro.sol.calculateSolarState
import kotlin.time.Instant

data class SolarPhaseState(
    val start: Instant,
    val end: Instant,
    val phase: SolarPhase,
    val startEvent: SolarEvent?,
    val endEvent: SolarEvent?
)

private data class SolarPhaseTime(
    val start: Instant,
    val startEvent: SolarEvent?,
    val phase: SolarPhase
)

fun List<SolarEvent>.solarPhaseStates(
    timeRange: ClosedRange<Instant>,
    location: Pair<Double, Double>
): List<SolarPhaseState> {
    return this
        .filter { it !is SolarEvent.LightEvent && it !is SolarEvent.HorizonEvent }
        .scan(
            SolarPhaseTime(
                start = timeRange.start,
                startEvent = null,
                phase = timeRange.start.calculateSolarState(location).solarPhase
            )
        ) { twilightElement, event -> computeTwilightElement(twilightElement, event) }
        .distinctUntilChangedBy { it }
        .plus(
            SolarPhaseTime(
                start = timeRange.endInclusive,
                startEvent = null,
                phase = timeRange.endInclusive.calculateSolarState(location).solarPhase
            )
        ).zipWithNext { firstPhase, secondPhase ->
            SolarPhaseState(
                start = firstPhase.start,
                end = secondPhase.start,
                phase = firstPhase.phase,
                startEvent = firstPhase.startEvent,
                endEvent = secondPhase.startEvent
            )
        }
}

private fun computeTwilightElement(currentPhase: SolarPhaseTime, event: SolarEvent) = when (event) {
    is SolarEvent.Sunrise -> currentPhase
    is SolarEvent.Sunset -> currentPhase
    is SolarEvent.SunriseEnd -> currentPhase
    is SolarEvent.SunsetBegin -> currentPhase
    is SolarEvent.BlueHourDawn -> currentPhase
    is SolarEvent.BlueHourDawnEnd -> currentPhase
    is SolarEvent.BlueHourDusk -> currentPhase
    is SolarEvent.BlueHourDuskEnd -> currentPhase
    is SolarEvent.GoldenHourDawn -> currentPhase
    is SolarEvent.GoldenHourDawnEnd -> currentPhase
    is SolarEvent.GoldenHourDusk -> currentPhase
    is SolarEvent.GoldenHourDuskEnd -> currentPhase
    is SolarEvent.AstronomicalDawn -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.AstronomicalDawn,
    )
    is SolarEvent.AstronomicalDusk -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.AstronomicalDusk
    )
    is SolarEvent.CivilDawn -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.CivilDawn
    )
    is SolarEvent.CivilDusk -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.CivilDusk
    )
    is SolarEvent.Day -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.Day
    )
    is SolarEvent.NauticalDawn -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.NauticalDawn
    )
    is SolarEvent.NauticalDusk -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.NauticalDusk
    )
    is SolarEvent.Night -> SolarPhaseTime(
        start = event.time,
        startEvent = event,
        phase = SolarPhase.Night
    )
    is SolarEvent.Nadir -> when (currentPhase.phase) {
        SolarPhase.AstronomicalDawn -> currentPhase
        SolarPhase.AstronomicalDusk -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.AstronomicalDawn
        )
        SolarPhase.CivilDawn -> currentPhase
        SolarPhase.CivilDusk -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.CivilDawn
        )
        SolarPhase.Day -> currentPhase
        SolarPhase.NauticalDawn -> currentPhase
        SolarPhase.NauticalDusk -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.NauticalDawn
        )
        SolarPhase.Night -> currentPhase
    }
    is SolarEvent.Noon -> when (currentPhase.phase) {
        SolarPhase.AstronomicalDawn -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.AstronomicalDusk
        )
        SolarPhase.AstronomicalDusk -> currentPhase
        SolarPhase.CivilDawn -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.CivilDusk
        )
        SolarPhase.CivilDusk -> currentPhase
        SolarPhase.Day -> currentPhase
        SolarPhase.NauticalDawn -> SolarPhaseTime(
            start = event.time,
            startEvent = event,
            phase = SolarPhase.NauticalDusk
        )
        SolarPhase.NauticalDusk -> currentPhase
        SolarPhase.Night -> currentPhase
    }
}
