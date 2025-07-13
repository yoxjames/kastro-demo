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

package dev.jamesyox.kastro.demo

import dev.jamesyox.kastro.demo.luna.lunarHorizonState
import dev.jamesyox.kastro.demo.misc.Location
import dev.jamesyox.kastro.demo.misc.asPair
import dev.jamesyox.kastro.demo.sol.solarLightState
import dev.jamesyox.kastro.demo.sol.solarPhaseStates
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.luna.LunarEventSequence
import dev.jamesyox.kastro.luna.LunarHorizonEventSequence
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.kastro.sol.SolarEventSequence
import dev.jamesyox.kastro.sol.SolarEventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toDeprecatedInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

data class CelestialParameters(
    val location: Location,
    val timeZone: TimeZone,
    val height: Double, // In Meters
    val date: LocalDate
) {
    val start = date.atStartOfDayIn(timeZone)
    val timeRange = start..(start + 1.days)
    val lunarEvents = LunarEventSequence(
        start = timeRange.start.toDeprecatedInstant(),
        location = location.asPair(),
        limit = timeRange.endInclusive - timeRange.start,
        requestedLunarEvents = LunarEvent.all
    ).toList()
    val lunarHorizonState = lunarEvents
        .toList()
        .lunarHorizonState(timeRange, location.asPair())
    val solarEvents = SolarEventSequence(
        start = timeRange.start.toDeprecatedInstant(),
        location = location.asPair(),
        limit = timeRange.endInclusive - timeRange.start,
        requestedSolarEvents = SolarEventType.all,
        height = height
    ).toList()

    val solarPhaseStates = solarEvents.solarPhaseStates(timeRange, location.asPair())
    val solarLightState = solarEvents.solarLightState(timeRange, location.asPair())
}

fun nextSolarHorizonEvent(parameters: CelestialParameters, time: Instant): Iterator<SolarEvent> {
    return SolarEventSequence(
        start = time.toDeprecatedInstant(),
        location = parameters.location.asPair(),
        limit = Duration.INFINITE,
        requestedSolarEvents = listOf(SolarEvent.Sunrise, SolarEvent.Sunset),
        height = parameters.height
    ).iterator()
}

fun nextLunarHorizonEvent(parameters: CelestialParameters, time: Instant): Iterator<LunarEvent> {
    return LunarHorizonEventSequence(
        start = time.toDeprecatedInstant(),
        location = parameters.location.asPair(),
        limit = Duration.INFINITE
    ).iterator()
}
